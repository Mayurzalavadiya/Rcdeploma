package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import rcdiploma.app.RetrofitClass.DeleteProfileData;
import rcdiploma.app.RetrofitClass.GetSignupData;
import rcdiploma.app.RetrofitClass.UpdateProfileData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsonProfileActivity extends AppCompatActivity {

    EditText name, email, contact, password;
    RadioGroup gender;
    RadioButton male, female, transgender;
    Spinner city;
    Button editProfile, submit, logout, deleteAccount;

    String sGender, sCity;
    String[] cityArray = {"Ahmedabad", "Anand", "Vadodara", "Surat"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,10}";

    SharedPreferences sp;
    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_profile);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(ConstantUrl.PREF, MODE_PRIVATE);
        name = findViewById(R.id.json_profile_name);
        email = findViewById(R.id.json_profile_email);
        contact = findViewById(R.id.json_profile_contact);
        password = findViewById(R.id.json_profile_password);

        name.setText(sp.getString(ConstantUrl.NAME, ""));
        email.setText(sp.getString(ConstantUrl.EMAIL, ""));
        contact.setText(sp.getString(ConstantUrl.CONTACT, ""));
        password.setText(sp.getString(ConstantUrl.PASSWORD, ""));

        gender = findViewById(R.id.json_profile_gender);
        male = findViewById(R.id.json_profile_male);
        female = findViewById(R.id.json_profile_female);
        transgender = findViewById(R.id.json_profile_transgender);

        sGender = sp.getString(ConstantUrl.GENDER, "");
        if (sGender.equalsIgnoreCase("Male")) {
            male.setChecked(true);
        } else if (sGender.equalsIgnoreCase("Female")) {
            female.setChecked(true);
        } else if (sGender.equalsIgnoreCase("Transgender")) {
            transgender.setChecked(true);
        } else {

        }

        city = findViewById(R.id.json_profile_city);
        editProfile = findViewById(R.id.json_profile_edit);
        submit = findViewById(R.id.json_profile_submit);
        logout = findViewById(R.id.json_profile_logout);
        deleteAccount = findViewById(R.id.json_profile_delete);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new ConnectionDetector(JsonProfileActivity.this).isConnectingToInternet()){
                    //new deleteAccountData().execute();
                    pd = new ProgressDialog(JsonProfileActivity.this);
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(false);
                    pd.show();
                    doDeleteData();
                }
                else{
                    new ConnectionDetector(JsonProfileActivity.this).connectiondetect();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new CommonMethodClass(JsonProfileActivity.this, JsonLoginActivity.class);
            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = gender.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(id);
                sGender = radioButton.getText().toString();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(JsonProfileActivity.this, android.R.layout.simple_list_item_1, cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        sCity = sp.getString(ConstantUrl.CITY, "");
        int selectedPosition = 0;
        for (int i = 0; i < cityArray.length; i++) {
            if (sCity.equalsIgnoreCase(cityArray[i])) {
                selectedPosition = i;
            }
        }
        city.setSelection(selectedPosition);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCity = cityArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equalsIgnoreCase("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().length() < 10 || contact.getText().toString().length() > 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Password Required");
                } else if (!password.getText().toString().matches(passwordPattern)) {
                    password.setError("Strong Password Required");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethodClass(JsonProfileActivity.this, "Please Select Gender");
                } else {
                    if (new ConnectionDetector(JsonProfileActivity.this).isConnectingToInternet()) {
                        //new updateProfile().execute();
                        pd = new ProgressDialog(JsonProfileActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doUpdateData();
                    } else {
                        new ConnectionDetector(JsonProfileActivity.this).connectiondetect();
                    }
                }
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);

                name.setEnabled(true);
                email.setEnabled(true);
                contact.setEnabled(true);
                password.setEnabled(true);

                male.setEnabled(true);
                female.setEnabled(true);
                transgender.setEnabled(true);
            }
        });

    }

    private void doDeleteData() {
        Call<DeleteProfileData> call = apiInterface.deleteProfileData(sp.getString(ConstantUrl.ID,""));

        call.enqueue(new Callback<DeleteProfileData>() {
            @Override
            public void onResponse(Call<DeleteProfileData> call, Response<DeleteProfileData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        new CommonMethodClass(JsonProfileActivity.this,response.body().message);
                        sp.edit().clear().commit();
                        new CommonMethodClass(JsonProfileActivity.this, JsonLoginActivity.class);
                    }
                    else{
                        new CommonMethodClass(JsonProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(JsonProfileActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<DeleteProfileData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(JsonProfileActivity.this,t.getMessage());
            }
        });
    }

    private void doUpdateData() {
        Call<UpdateProfileData> call = apiInterface.updateProfileData(sp.getString(ConstantUrl.ID,""),name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                contact.getText().toString(),
                sGender,
                sCity);

        call.enqueue(new Callback<UpdateProfileData>() {
            @Override
            public void onResponse(Call<UpdateProfileData> call, Response<UpdateProfileData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        new CommonMethodClass(JsonProfileActivity.this,response.body().message);
                        sp.edit().putString(ConstantUrl.NAME, name.getText().toString()).commit();
                        sp.edit().putString(ConstantUrl.EMAIL, email.getText().toString()).commit();
                        sp.edit().putString(ConstantUrl.CONTACT, contact.getText().toString()).commit();
                        sp.edit().putString(ConstantUrl.PASSWORD, password.getText().toString()).commit();
                        sp.edit().putString(ConstantUrl.CITY, sCity).commit();
                        sp.edit().putString(ConstantUrl.GENDER, sGender).commit();

                        editProfile.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);

                        name.setEnabled(false);
                        email.setEnabled(false);
                        contact.setEnabled(false);
                        password.setEnabled(false);

                        male.setEnabled(false);
                        female.setEnabled(false);
                        transgender.setEnabled(false);
                    }
                    else{
                        new CommonMethodClass(JsonProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(JsonProfileActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(JsonProfileActivity.this,t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private class updateProfile extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(JsonProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", sp.getString(ConstantUrl.ID, ""));
            hashMap.put("username", name.getText().toString());
            hashMap.put("emailid", email.getText().toString());
            hashMap.put("password", password.getText().toString());
            hashMap.put("contact", contact.getText().toString());
            hashMap.put("gender", sGender);
            hashMap.put("city", sCity);
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL + "updateProfile.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
                    new CommonMethodClass(JsonProfileActivity.this, object.getString("Message"));

                    sp.edit().putString(ConstantUrl.NAME, name.getText().toString()).commit();
                    sp.edit().putString(ConstantUrl.EMAIL, email.getText().toString()).commit();
                    sp.edit().putString(ConstantUrl.CONTACT, contact.getText().toString()).commit();
                    sp.edit().putString(ConstantUrl.PASSWORD, password.getText().toString()).commit();
                    sp.edit().putString(ConstantUrl.CITY, sCity).commit();
                    sp.edit().putString(ConstantUrl.GENDER, sGender).commit();

                    editProfile.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);

                    name.setEnabled(false);
                    email.setEnabled(false);
                    contact.setEnabled(false);
                    password.setEnabled(false);

                    male.setEnabled(false);
                    female.setEnabled(false);
                    transgender.setEnabled(false);
                } else {
                    new CommonMethodClass(JsonProfileActivity.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class deleteAccountData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(JsonProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("id",sp.getString(ConstantUrl.ID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"deleteAccount.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equalsIgnoreCase("True")){
                    new CommonMethodClass(JsonProfileActivity.this,object.getString("Message"));
                    sp.edit().clear().commit();
                    new CommonMethodClass(JsonProfileActivity.this, JsonLoginActivity.class);
                }
                else{
                    new CommonMethodClass(JsonProfileActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}