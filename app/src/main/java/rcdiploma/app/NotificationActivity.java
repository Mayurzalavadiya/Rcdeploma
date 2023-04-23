package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import rcdiploma.app.RetrofitClass.AddFcmData;
import rcdiploma.app.RetrofitClass.GetNotificationData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import rcdiploma.app.notifications.Config;
import rcdiploma.app.notifications.NotificationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId;
    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;

    RecyclerView recyclerView;
    ArrayList<NotificationList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);

        recyclerView = findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //Update FCM ID CODE
                    updateFCM();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                }
            }
        };
        updateFCM();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void updateFCM() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                regId = newToken;
                Log.e("newToken", newToken);
                sp.edit().putString(ConstantUrl.FCM_ID, newToken).commit();
                if(sp.getString(ConstantUrl.FCM_USER_ID,"").equals("")) {
                    pd = new ProgressDialog(NotificationActivity.this);
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(false);
                    pd.show();
                    addFcmData();
                }
                else{
                    //Log.d("RESPONSE_ELSE","Already Registered FCM Id");
                    if(new ConnectionDetector(NotificationActivity.this).isConnectingToInternet()){
                        pd = new ProgressDialog(NotificationActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        getNotification();
                    }
                    else{
                        new ConnectionDetector(NotificationActivity.this).connectiondetect();
                    }
                }
            }
        });
    }

    private void getNotification() {
        Call<GetNotificationData> call = apiInterface.getNotificationData(sp.getString(ConstantUrl.FCM_USER_ID,""));
        call.enqueue(new Callback<GetNotificationData>() {
            @Override
            public void onResponse(Call<GetNotificationData> call, Response<GetNotificationData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        arrayList = new ArrayList<>();
                        GetNotificationData data = response.body();
                        for(int i=0;i<data.response.size();i++){
                            NotificationList list = new NotificationList();
                            list.setId(data.response.get(i).id);
                            list.setMessage(data.response.get(i).message);
                            list.setIsRead(data.response.get(i).isRead);
                            list.setCreateDate(data.response.get(i).createdDate);
                            arrayList.add(list);
                        }
                        NotificationAdapter adapter = new NotificationAdapter(NotificationActivity.this,arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(NotificationActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(NotificationActivity.this,ConstantUrl.SERVER_ERROR+" "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetNotificationData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(NotificationActivity.this,t.getMessage());
            }
        });
    }

    private void addFcmData() {
        Call<AddFcmData> call = apiInterface.addFcmData(sp.getString(ConstantUrl.FCM_ID,""));
        call.enqueue(new Callback<AddFcmData>() {
            @Override
            public void onResponse(Call<AddFcmData> call, Response<AddFcmData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        Log.d("RESPONSE_FCM",response.body().message+"\n"+response.body().id);
                        sp.edit().putString(ConstantUrl.FCM_USER_ID,response.body().id).commit();
                    }
                    else{
                        new CommonMethodClass(NotificationActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(NotificationActivity.this,ConstantUrl.SERVER_ERROR+" "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AddFcmData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(NotificationActivity.this,t.getMessage());
            }
        });
    }

}