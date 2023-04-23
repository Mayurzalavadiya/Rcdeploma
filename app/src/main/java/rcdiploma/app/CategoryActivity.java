package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rcdiploma.app.RetrofitClass.GetBannerData;
import rcdiploma.app.RetrofitClass.GetCategoryData;
import rcdiploma.app.RetrofitClass.GetLoginData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add;
    ArrayList<CategoryList> arrayList;
    CategoryAdapter adapter;

    ImageSlider slider;

    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setTitle("Category");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        add = findViewById(R.id.category_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(CategoryActivity.this,ImageUploadActivity.class);
            }
        });

        slider = findViewById(R.id.category_banner);

        /*slider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {

            }
        });*/

        recyclerView = findViewById(R.id.category_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(new ConnectionDetector(CategoryActivity.this).isConnectingToInternet()){
            //new getData().execute();
            //new getBanner().execute();
            doBanner();
            pd = new ProgressDialog(CategoryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            doCategory();
        }
        else{
            new ConnectionDetector(CategoryActivity.this).connectiondetect();
        }

    }

    private void doCategory() {
        Call<GetCategoryData> call = apiInterface.getCategoryData();
        call.enqueue(new Callback<GetCategoryData>() {
            @Override
            public void onResponse(Call<GetCategoryData> call, Response<GetCategoryData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        GetCategoryData data = response.body();
                        arrayList = new ArrayList<>();
                        for(int i=0;i<data.response.size();i++){
                            CategoryList list = new CategoryList();
                            list.setId(data.response.get(i).id);
                            list.setName(data.response.get(i).name);
                            list.setImage(data.response.get(i).image);
                            arrayList.add(list);
                        }
                        adapter = new CategoryAdapter(CategoryActivity.this,arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(CategoryActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(CategoryActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetCategoryData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(CategoryActivity.this,t.getMessage());
            }
        });
    }


    private void doBanner() {
        Call<GetBannerData> call = apiInterface.getBannerData();
        call.enqueue(new Callback<GetBannerData>() {
            @Override
            public void onResponse(Call<GetBannerData> call, Response<GetBannerData> response) {
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        GetBannerData data = response.body();
                        ArrayList<SlideModel> arrayList = new ArrayList<>();
                        for(int i=0;i<data.response.size();i++){
                            arrayList.add(new SlideModel(data.response.get(i).image, ScaleTypes.FIT));
                            slider.setImageList(arrayList);
                        }
                    }
                    else{
                        new CommonMethodClass(CategoryActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(CategoryActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetBannerData> call, Throwable t) {
                new CommonMethodClass(CategoryActivity.this,t.getMessage());
            }
        });
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(CategoryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"getCategory.php",MakeServiceCall.POST,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equalsIgnoreCase("True")){
                    JSONArray array = object.getJSONArray("response");
                    arrayList = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        CategoryList list = new CategoryList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("name"));
                        list.setImage(jsonObject.getString("image"));
                        arrayList.add(list);
                    }
                    adapter = new CategoryAdapter(CategoryActivity.this,arrayList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    new CommonMethodClass(CategoryActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

        Context context;
        ArrayList<CategoryList> arrayList;
        SharedPreferences sp;

        public CategoryAdapter(CategoryActivity categoryActivity, ArrayList<CategoryList> arrayList) {
            this.context = categoryActivity;
            this.arrayList = arrayList;
            sp = context.getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category,parent,false);
            return new MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_category_image);
                name = itemView.findViewById(R.id.custom_category_name);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString(ConstantUrl.CATEGORY_ID,arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.CATEGORY_NAME,arrayList.get(position).getName()).commit();
                    new CommonMethodClass(context,SubCategoryActivity.class);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    private class getBanner extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CategoryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"getBanner.php",MakeServiceCall.POST,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equalsIgnoreCase("True")){
                    JSONArray array = object.getJSONArray("response");
                    ArrayList<SlideModel> arrayList = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        arrayList.add(new SlideModel(jsonObject.getString("image"), ScaleTypes.FIT));
                        slider.setImageList(arrayList);
                    }
                }
                else{
                    new CommonMethodClass(CategoryActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}