package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rcdiploma.app.RetrofitClass.AddRemoveWishlistData;
import rcdiploma.app.RetrofitClass.GetWishlistData;
import rcdiploma.app.RetrofitClass.GetWishlistData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;

    ArrayList<ProductList> arrayList;
    ProductAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        
        sp = getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        
        recyclerView = findViewById(R.id.wishlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(new ConnectionDetector(WishlistActivity.this).isConnectingToInternet()){
            pd = new ProgressDialog(WishlistActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            doWishlistData();
            //new getData().execute();
        }
        else{
            new ConnectionDetector(WishlistActivity.this).connectiondetect();
        }
    }

    private void doWishlistData() {
        Call<GetWishlistData> call = apiInterface.getWishlistData(sp.getString(ConstantUrl.ID,""));
        call.enqueue(new Callback<GetWishlistData>() {
            @Override
            public void onResponse(Call<GetWishlistData> call, Response<GetWishlistData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        GetWishlistData data = response.body();
                        arrayList = new ArrayList<>();
                        for(int i=0;i<data.response.size();i++){
                            ProductList list = new ProductList();
                            list.setId(data.response.get(i).productId);
                            list.setName(data.response.get(i).name);
                            list.setPrice(data.response.get(i).price);
                            list.setUnit(data.response.get(i).unit);
                            list.setDescription(data.response.get(i).description);
                            list.setImage(data.response.get(i).image);
                            arrayList.add(list);
                        }
                        adapter = new ProductAdapter(WishlistActivity.this,arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                    else{
                        new CommonMethodClass(WishlistActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(WishlistActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetWishlistData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(WishlistActivity.this,t.getMessage());
            }
        });
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

        Context context;
        ArrayList<ProductList> arrayList;
        SharedPreferences sp;

        public ProductAdapter(WishlistActivity categoryActivity, ArrayList<ProductList> arrayList) {
            this.context = categoryActivity;
            this.arrayList = arrayList;
            sp = context.getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        }

        @NonNull
        @Override
        public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wishlist,parent,false);
            return new ProductAdapter.MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            ImageView imageView,wishlistFill;
            TextView name,price;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.custom_wishlist_image);
                name = itemView.findViewById(R.id.custom_wishlist_name);
                price = itemView.findViewById(R.id.custom_wishlist_price);
                wishlistFill = itemView.findViewById(R.id.custom_wishlist_wishlist_fill);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.price.setText(getResources().getString(R.string.price_symbol)+arrayList.get(position).getPrice()+"/"+arrayList.get(position).getUnit());
            Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);

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
                            arrayList.remove(position);
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