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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rcdiploma.app.RetrofitClass.AddRemoveWishlistData;
import rcdiploma.app.RetrofitClass.GetProductData;
import rcdiploma.app.RetrofitClass.GetSubCategoryData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add;
    ArrayList<ProductList> arrayList;
    ProductAdapter adapter;
    SharedPreferences sp;

    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        sp = getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        getSupportActionBar().setTitle(sp.getString(ConstantUrl.SUB_CATEGORY_NAME,""));
        /*add = findViewById(R.id.category_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(ProductActivity.this,ImageUploadActivity.class);
            }
        });*/

        recyclerView = findViewById(R.id.product_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(new ConnectionDetector(ProductActivity.this).isConnectingToInternet()){
            pd = new ProgressDialog(ProductActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            doProductData();
            //new getData().execute();
        }
        else{
            new ConnectionDetector(ProductActivity.this).connectiondetect();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wishlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_wishlist){
            new CommonMethodClass(ProductActivity.this,WishlistActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void doProductData() {
        Call<GetProductData> call = apiInterface.getProductData(sp.getString(ConstantUrl.SUB_CATEGORY_ID,""),sp.getString(ConstantUrl.ID,""));
        call.enqueue(new Callback<GetProductData>() {
            @Override
            public void onResponse(Call<GetProductData> call, Response<GetProductData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        GetProductData data = response.body();
                        arrayList = new ArrayList<>();
                        for(int i=0;i<data.response.size();i++){
                            ProductList list = new ProductList();
                            list.setId(data.response.get(i).id);
                            list.setName(data.response.get(i).name);
                            list.setPrice(data.response.get(i).price);
                            list.setUnit(data.response.get(i).unit);
                            list.setDescription(data.response.get(i).description);
                            list.setImage(data.response.get(i).image);
                            list.setWishlistFlag(data.response.get(i).wishlistFlag);
                            arrayList.add(list);
                        }
                        adapter = new ProductAdapter(ProductActivity.this,arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(ProductActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(ProductActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetProductData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(ProductActivity.this,t.getMessage());
            }
        });
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(ProductActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("subCategoryId",sp.getString(ConstantUrl.SUB_CATEGORY_ID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"getProduct.php",MakeServiceCall.POST,hashMap);
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
                        ProductList list = new ProductList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("name"));
                        list.setPrice(jsonObject.getString("price"));
                        list.setUnit(jsonObject.getString("unit"));
                        list.setDescription(jsonObject.getString("description"));
                        list.setImage(jsonObject.getString("image"));
                        arrayList.add(list);
                    }
                    adapter = new ProductAdapter(ProductActivity.this,arrayList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    new CommonMethodClass(ProductActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

        Context context;
        ArrayList<ProductList> arrayList;
        SharedPreferences sp;

        public ProductAdapter(ProductActivity categoryActivity, ArrayList<ProductList> arrayList) {
            this.context = categoryActivity;
            this.arrayList = arrayList;
            sp = context.getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        }

        @NonNull
        @Override
        public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
            return new ProductAdapter.MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            ImageView imageView,wishlistFill,wishlistBlank;
            TextView name,price;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_product_image);
                name = itemView.findViewById(R.id.custom_product_name);
                price = itemView.findViewById(R.id.custom_product_price);
                wishlistBlank = itemView.findViewById(R.id.custom_product_wishlist_blank);
                wishlistFill = itemView.findViewById(R.id.custom_product_wishlist_fill);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.price.setText(getResources().getString(R.string.price_symbol)+arrayList.get(position).getPrice()+"/"+arrayList.get(position).getUnit());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

            if(arrayList.get(position).getWishlistFlag().equalsIgnoreCase("Yes")){
                holder.wishlistFill.setVisibility(View.VISIBLE);
                holder.wishlistBlank.setVisibility(View.GONE);
            }
            else{
                holder.wishlistFill.setVisibility(View.GONE);
                holder.wishlistBlank.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString(ConstantUrl.PRODUCT_ID,arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_NAME,arrayList.get(position).getName()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_PRICE,getResources().getString(R.string.price_symbol)+arrayList.get(position).getPrice()+"/"+arrayList.get(position).getUnit()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_DESCRIPTION,arrayList.get(position).getDescription()).commit();
                    sp.edit().putString(ConstantUrl.PRODUCT_IMAGE,arrayList.get(position).getImage()).commit();
                    new CommonMethodClass(context,ProductDetailActivity.class);
                }
            });

            holder.wishlistBlank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(new ConnectionDetector(context).isConnectingToInternet()) {
                        doWishlist(position);
                    }
                    else{
                        new ConnectionDetector(context).connectiondetect();
                    }
                }
            });

            holder.wishlistFill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(new ConnectionDetector(context).isConnectingToInternet()) {
                        doWishlist(position);
                    }
                    else{
                        new ConnectionDetector(context).connectiondetect();
                    }
                }
            });

        }

        private void doWishlist(int position) {
            Call<AddRemoveWishlistData> call = apiInterface.addRemoveWishlistData(arrayList.get(position).getId(),sp.getString(ConstantUrl.ID,""));
            call.enqueue(new Callback<AddRemoveWishlistData>() {
                @Override
                public void onResponse(Call<AddRemoveWishlistData> call, Response<AddRemoveWishlistData> response) {
                    if(response.code()==200){
                        if(response.body().status.equalsIgnoreCase("True")){
                            new CommonMethodClass(context,response.body().message);
                            ProductList list = new ProductList();
                            list.setId(arrayList.get(position).getId());
                            list.setName(arrayList.get(position).getName());
                            list.setPrice(arrayList.get(position).getPrice());
                            list.setUnit(arrayList.get(position).getUnit());
                            list.setDescription(arrayList.get(position).getDescription());
                            list.setImage(arrayList.get(position).getImage());
                            list.setWishlistFlag(response.body().wishlistFlag);
                            arrayList.set(position,list);
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            new CommonMethodClass(context,response.body().message);
                        }
                    }
                    else{
                        new CommonMethodClass(context,"Server Error : "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<AddRemoveWishlistData> call, Throwable t) {
                    new CommonMethodClass(context,t.getMessage());
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}