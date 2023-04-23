package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rcdiploma.app.RetrofitClass.GetCategoryData;
import rcdiploma.app.RetrofitClass.GetSubCategoryData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add;
    ArrayList<SubCategoryList> arrayList;
    SubCategoryAdapter adapter;
    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        getSupportActionBar().setTitle(sp.getString(ConstantUrl.CATEGORY_NAME,""));
        /*add = findViewById(R.id.category_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(SubCategoryActivity.this,ImageUploadActivity.class);
            }
        });*/

        recyclerView = findViewById(R.id.sub_category_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(new ConnectionDetector(SubCategoryActivity.this).isConnectingToInternet()){
            //new getData().execute();
            pd = new ProgressDialog(SubCategoryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            doSubCategory();
        }
        else{
            new ConnectionDetector(SubCategoryActivity.this).connectiondetect();
        }

    }

    private void doSubCategory() {
        Call<GetSubCategoryData> call = apiInterface.getSubCategoryData(sp.getString(ConstantUrl.CATEGORY_ID,""));
        call.enqueue(new Callback<GetSubCategoryData>() {
            @Override
            public void onResponse(Call<GetSubCategoryData> call, Response<GetSubCategoryData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        GetSubCategoryData data = response.body();
                        arrayList = new ArrayList<>();
                        for(int i=0;i<data.response.size();i++){
                            SubCategoryList list = new SubCategoryList();
                            list.setId(data.response.get(i).id);
                            list.setName(data.response.get(i).name);
                            list.setImage(data.response.get(i).image);
                            arrayList.add(list);
                        }
                        adapter = new SubCategoryAdapter(SubCategoryActivity.this,arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(SubCategoryActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(SubCategoryActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSubCategoryData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(SubCategoryActivity.this,t.getMessage());
            }
        });
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(SubCategoryActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("categoryId",sp.getString(ConstantUrl.CATEGORY_ID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"getSubCategory.php",MakeServiceCall.POST,hashMap);
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
                        SubCategoryList list = new SubCategoryList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("name"));
                        list.setImage(jsonObject.getString("image"));
                        arrayList.add(list);
                    }
                    adapter = new SubCategoryAdapter(SubCategoryActivity.this,arrayList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    new CommonMethodClass(SubCategoryActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {

        Context context;
        ArrayList<SubCategoryList> arrayList;
        SharedPreferences sp;

        public SubCategoryAdapter(SubCategoryActivity categoryActivity, ArrayList<SubCategoryList> arrayList) {
            this.context = categoryActivity;
            this.arrayList = arrayList;
            sp = context.getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        }

        @NonNull
        @Override
        public SubCategoryAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_subcategory,parent,false);
            return new SubCategoryAdapter.MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView name;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_subcategory_image);
                name = itemView.findViewById(R.id.custom_subcategory_name);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull SubCategoryAdapter.MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString(ConstantUrl.SUB_CATEGORY_ID,arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.SUB_CATEGORY_NAME,arrayList.get(position).getName()).commit();
                    new CommonMethodClass(context,ProductActivity.class);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}