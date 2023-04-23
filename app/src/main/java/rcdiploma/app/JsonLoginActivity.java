package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import rcdiploma.app.RetrofitClass.GetLoginData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsonLoginActivity extends AppCompatActivity {

    EditText email, password;
    TextView createAccount;
    Button login;

    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_login);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
        email = findViewById(R.id.json_login_email);
        password = findViewById(R.id.json_login_password);
        createAccount = findViewById(R.id.json_login_create_account);
        login = findViewById(R.id.json_login_submit);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(JsonLoginActivity.this, JsonSignupActivity.class);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Id Required");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Password Required");
                } else {
                    if (new ConnectionDetector(JsonLoginActivity.this).isConnectingToInternet()) {
                        //new loginData().execute();
                        pd = new ProgressDialog(JsonLoginActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doLogin();
                    } else {
                        new ConnectionDetector(JsonLoginActivity.this).connectiondetect();
                    }
                }
            }
        });

    }

    private void doLogin() {
        Call<GetLoginData> call = apiInterface.getLoginData(email.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<GetLoginData>() {
            @Override
            public void onResponse(Call<GetLoginData> call, Response<GetLoginData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        new CommonMethodClass(JsonLoginActivity.this,response.body().message);
                        GetLoginData data = response.body();
                        for (int i = 0; i < data.response.size(); i++) {
                            sp.edit().putString(ConstantUrl.ID, data.response.get(i).id).commit();
                            sp.edit().putString(ConstantUrl.NAME, data.response.get(i).name).commit();
                            sp.edit().putString(ConstantUrl.EMAIL, data.response.get(i).email).commit();
                            sp.edit().putString(ConstantUrl.CONTACT, data.response.get(i).contact).commit();
                            sp.edit().putString(ConstantUrl.PASSWORD, data.response.get(i).password).commit();
                            sp.edit().putString(ConstantUrl.GENDER, data.response.get(i).gender).commit();
                            sp.edit().putString(ConstantUrl.CITY, data.response.get(i).city).commit();
                            //new CommonMethodClass(JsonLoginActivity.this,JsonProfileActivity.class);
                            new CommonMethodClass(JsonLoginActivity.this,CategoryActivity.class);
                        }
                    }
                    else{
                        new CommonMethodClass(JsonLoginActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(JsonLoginActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetLoginData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(JsonLoginActivity.this,t.getMessage());
            }
        });
    }

    private class loginData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(JsonLoginActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("emailid", email.getText().toString());
            hashMap.put("password", password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL + "login.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
                    new CommonMethodClass(JsonLoginActivity.this,object.getString("Message"));
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        sp.edit().putString(ConstantUrl.ID, jsonObject.getString("id")).commit();
                        sp.edit().putString(ConstantUrl.NAME, jsonObject.getString("name")).commit();
                        sp.edit().putString(ConstantUrl.EMAIL, jsonObject.getString("email")).commit();
                        sp.edit().putString(ConstantUrl.CONTACT, jsonObject.getString("contact")).commit();
                        sp.edit().putString(ConstantUrl.PASSWORD, jsonObject.getString("password")).commit();
                        sp.edit().putString(ConstantUrl.GENDER, jsonObject.getString("gender")).commit();
                        sp.edit().putString(ConstantUrl.CITY, jsonObject.getString("city")).commit();
                        new CommonMethodClass(JsonLoginActivity.this,JsonProfileActivity.class);
                    }
                } else {
                    new CommonMethodClass(JsonLoginActivity.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}