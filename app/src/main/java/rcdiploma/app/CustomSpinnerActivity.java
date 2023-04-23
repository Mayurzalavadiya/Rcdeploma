package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import rcdiploma.app.RetrofitClass.GetCountryData;
import rcdiploma.app.RetrofitClass.GetStateData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomSpinnerActivity extends AppCompatActivity {

    Spinner spinner,stateSpinner;
    String[] nameArray = {"Android", "Java", "Php", "IOS", "Flutter", "React Native"};
    int[] imageArray = {R.drawable.android_image, R.drawable.java, R.drawable.calendar_icon, R.drawable.clock_24, R.drawable.flutter_logo, R.drawable.delete};

    ArrayList<CustomSpinnerList> arrayList;
    ArrayList<StateSpinnerList> stateArrayList;

    ApiInterface apiInterface;
    ProgressDialog pd;

    String sCountryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_spinner);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spinner = findViewById(R.id.custom_spinner);
        stateSpinner = findViewById(R.id.custom_spinner_state);

        if (new ConnectionDetector(CustomSpinnerActivity.this).isConnectingToInternet()) {
            pd = new ProgressDialog(CustomSpinnerActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            getCountryData();
        } else {
            new ConnectionDetector(CustomSpinnerActivity.this).connectiondetect();
        }

    }

    private void getCountryData() {
        Call<GetCountryData> call = apiInterface.getCountryData();
        call.enqueue(new Callback<GetCountryData>() {
            @Override
            public void onResponse(Call<GetCountryData> call, Response<GetCountryData> response) {
                pd.dismiss();
                if (response.code() == 200) {
                    if (response.body().status.equalsIgnoreCase("True")) {
                        arrayList = new ArrayList<>();
                        GetCountryData data = response.body();
                        for (int i = 0; i < data.response.size(); i++) {
                            CustomSpinnerList list = new CustomSpinnerList();
                            list.setId(data.response.get(i).id);
                            list.setName(data.response.get(i).name);
                            list.setImage(data.response.get(i).image);
                            arrayList.add(list);
                        }
                        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(CustomSpinnerActivity.this, arrayList);
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                new CommonMethodClass(CustomSpinnerActivity.this, arrayList.get(i).getName());
                                sCountryId = arrayList.get(i).getId();
                                if (new ConnectionDetector(CustomSpinnerActivity.this).isConnectingToInternet()) {
                                    pd = new ProgressDialog(CustomSpinnerActivity.this);
                                    pd.setMessage("Please Wait...");
                                    pd.setCancelable(false);
                                    pd.show();
                                    getStateData();
                                } else {
                                    new ConnectionDetector(CustomSpinnerActivity.this).connectiondetect();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        new CommonMethodClass(CustomSpinnerActivity.this, response.body().message);
                    }
                } else {
                    new CommonMethodClass(CustomSpinnerActivity.this, "Server Error : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetCountryData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(CustomSpinnerActivity.this, t.getMessage());
            }
        });
    }

    private void getStateData() {
        Call<GetStateData> call = apiInterface.getStateData(sCountryId);
        call.enqueue(new Callback<GetStateData>() {
            @Override
            public void onResponse(Call<GetStateData> call, Response<GetStateData> response) {
                pd.dismiss();
                if (response.code() == 200) {
                    if (response.body().status.equalsIgnoreCase("True")) {
                        stateArrayList = new ArrayList<>();
                        GetStateData data = response.body();
                        for (int i = 0; i < data.response.size(); i++) {
                            StateSpinnerList list = new StateSpinnerList();
                            list.setId(data.response.get(i).id);
                            list.setName(data.response.get(i).name);
                            list.setImage(data.response.get(i).image);
                            stateArrayList.add(list);
                        }
                        CustomStateSpinnerAdapter adapter = new CustomStateSpinnerAdapter(CustomSpinnerActivity.this, stateArrayList);
                        stateSpinner.setAdapter(adapter);

                        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                new CommonMethodClass(CustomSpinnerActivity.this, stateArrayList.get(i).getName());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        new CommonMethodClass(CustomSpinnerActivity.this, response.body().message);
                    }
                } else {
                    new CommonMethodClass(CustomSpinnerActivity.this, "Server Error : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetStateData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(CustomSpinnerActivity.this, t.getMessage());
            }
        });
    }

}