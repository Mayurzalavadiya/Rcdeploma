package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.ArrayList;

import rcdiploma.app.RetrofitClass.GetVideoData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListActivity extends AppCompatActivity {

    //RecyclerView recyclerView;
    ViewPager2 viewPager2;
    ApiInterface apiService;
    ProgressDialog pd;

    ArrayList<VideoList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        /*recyclerView = findViewById(R.id.video_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());*/

        viewPager2 = findViewById(R.id.video_list_viewpager);

        if (new ConnectionDetector(VideoListActivity.this).isConnectingToInternet()) {
            pd = new ProgressDialog(VideoListActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getData();
        } else {
            new ConnectionDetector(VideoListActivity.this).connectiondetect();
        }
    }

    private void getData() {
        Call<GetVideoData> call = apiService.getVideoData();
        call.enqueue(new Callback<GetVideoData>() {
            @Override
            public void onResponse(Call<GetVideoData> call, Response<GetVideoData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        arrayList = new ArrayList<>();
                        GetVideoData data = response.body();
                        for(int i=0;i<data.response.size();i++){
                            VideoList list = new VideoList();
                            list.setId(data.response.get(i).id);
                            list.setTitle(data.response.get(i).title);
                            list.setDescription(data.response.get(i).description);
                            list.setVideo(data.response.get(i).video);
                            list.setDate(data.response.get(i).createdDate);
                            arrayList.add(list);
                        }
                        VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this,arrayList);
                        viewPager2.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(VideoListActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(VideoListActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetVideoData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(VideoListActivity.this,t.getMessage());
            }
        });
    }

    /*private void getData() {
        Call<GetVideoData> call = apiService.getVideoData();
        call.enqueue(new Callback<GetVideoData>() {
            @Override
            public void onResponse(Call<GetVideoData> call, Response<GetVideoData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        arrayList = new ArrayList<>();
                        GetVideoData data = response.body();
                        for(int i=0;i<data.response.size();i++){
                            VideoList list = new VideoList();
                            list.setId(data.response.get(i).id);
                            list.setTitle(data.response.get(i).title);
                            list.setDescription(data.response.get(i).description);
                            list.setVideo(data.response.get(i).video);
                            list.setDate(data.response.get(i).createdDate);
                            arrayList.add(list);
                        }
                        VideoListAdapter adapter = new VideoListAdapter(VideoListActivity.this,arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(VideoListActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(VideoListActivity.this,"Server Error Code : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetVideoData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(VideoListActivity.this,t.getMessage());
            }
        });
    }*/
}