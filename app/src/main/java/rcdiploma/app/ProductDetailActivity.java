package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rcdiploma.app.RetrofitClass.GetProductData;
import rcdiploma.app.RetrofitClass.GetProductDetailData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    SharedPreferences sp;
    TextView name,description,price;
    ImageView imageView;

    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        getSupportActionBar().setTitle(sp.getString(ConstantUrl.PRODUCT_NAME,""));
        name = findViewById(R.id.product_detail_name);
        price = findViewById(R.id.product_detail_price);
        description = findViewById(R.id.product_detail_description);
        imageView = findViewById(R.id.product_detail_image);

        /*Picasso.get().load(sp.getString(ConstantUrl.PRODUCT_IMAGE,"")).placeholder(R.mipmap.ic_launcher).into(imageView);
        name.setText(sp.getString(ConstantUrl.PRODUCT_NAME,""));
        price.setText(sp.getString(ConstantUrl.PRODUCT_PRICE,""));
        description.setText(sp.getString(ConstantUrl.PRODUCT_DESCRIPTION,""));*/

        if(new ConnectionDetector(ProductDetailActivity.this).isConnectingToInternet()){
            //new getData().execute();
            pd = new ProgressDialog(ProductDetailActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            doProductDetailData();
        }
        else{
            new ConnectionDetector(ProductDetailActivity.this).connectiondetect();
        }

    }

    private void doProductDetailData() {
        Call<GetProductDetailData> call = apiInterface.getProductDetailData(sp.getString(ConstantUrl.PRODUCT_ID,""));
        call.enqueue(new Callback<GetProductDetailData>() {
            @Override
            public void onResponse(Call<GetProductDetailData> call, Response<GetProductDetailData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equals("True")){
                        GetProductDetailData data = response.body();
                        for(int i=0;i<data.response.size();i++){
                            name.setText(data.response.get(i).name);
                            price.setText(getResources().getString(R.string.price_symbol)+data.response.get(i).price+"/"+data.response.get(i).unit);
                            description.setText(data.response.get(i).description);
                            Picasso.get().load(data.response.get(i).image).placeholder(R.mipmap.ic_launcher).into(imageView);
                        }
                    }
                    else{
                        new CommonMethodClass(ProductDetailActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(ProductDetailActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetProductDetailData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(ProductDetailActivity.this,t.getMessage());
            }
        });
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(ProductDetailActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("productId",sp.getString(ConstantUrl.PRODUCT_ID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"getProductDetail.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equalsIgnoreCase("True")){
                    JSONArray array = object.getJSONArray("response");
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        name.setText(jsonObject.getString("name"));
                        price.setText(getResources().getString(R.string.price_symbol)+jsonObject.getString("price")+"/"+jsonObject.getString("unit"));
                        description.setText(jsonObject.getString("description"));
                        Picasso.get().load(jsonObject.getString("image")).placeholder(R.mipmap.ic_launcher).into(imageView);
                    }
                }
                else{
                    new CommonMethodClass(ProductDetailActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}