package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import rcdiploma.app.RetrofitClass.GetSignupData;
import rcdiploma.app.Utils.ApiClient;
import rcdiploma.app.Utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsonSignupActivity extends AppCompatActivity {

    EditText name, email, contact, password, confirmPassword;
    RadioGroup gender;
    Spinner city;
    Button submit;

    String sGender, sCity;
    String[] cityArray = {"Ahmedabad", "Anand", "Vadodara", "Surat"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,10}";

    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_signup);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        name = findViewById(R.id.json_signup_name);
        email = findViewById(R.id.json_signup_email);
        contact = findViewById(R.id.json_signup_contact);
        password = findViewById(R.id.json_signup_password);
        confirmPassword = findViewById(R.id.json_signup_confirm_password);

        gender = findViewById(R.id.json_signup_gender);
        city = findViewById(R.id.json_signup_city);
        submit = findViewById(R.id.json_signup_submit);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = gender.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(id);
                sGender = radioButton.getText().toString();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(JsonSignupActivity.this, android.R.layout.simple_list_item_1, cityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

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
                } else if (confirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (!confirmPassword.getText().toString().matches(passwordPattern)) {
                    confirmPassword.setError("Strong Password Required");
                } else if (!password.getText().toString().matches(confirmPassword.getText().toString())) {
                    confirmPassword.setError("Password Does Not Match");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethodClass(JsonSignupActivity.this, "Please Select Gender");
                } else {
                    if (new ConnectionDetector(JsonSignupActivity.this).isConnectingToInternet()) {
                        //new CommonMethodClass(JsonSignupActivity.this,"Internet/Wifi Connected");
                        //new signupData().execute();
                        pd = new ProgressDialog(JsonSignupActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        signupData();
                    } else {
                        new ConnectionDetector(JsonSignupActivity.this).connectiondetect();
                    }
                }
            }
        });

    }

    private void signupData() {
        Call<GetSignupData> call = apiInterface.addSignupData(name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                contact.getText().toString(),
                sGender,
                sCity);

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status.equalsIgnoreCase("True")){
                        new CommonMethodClass(JsonSignupActivity.this,response.body().message);
                        onBackPressed();
                    }
                    else{
                        new CommonMethodClass(JsonSignupActivity.this,response.body().message);
                    }
                }
                else{
                    new CommonMethodClass(JsonSignupActivity.this,ConstantUrl.SERVER_ERROR+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                new CommonMethodClass(JsonSignupActivity.this,t.getMessage());
            }
        });
    }

    /*private class signupData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(JsonSignupActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("username",name.getText().toString());
            hashMap.put("emailid",email.getText().toString());
            hashMap.put("password",password.getText().toString());
            hashMap.put("contact",contact.getText().toString());
            hashMap.put("gender",sGender);
            hashMap.put("city",sCity);
            return new MakeServiceCall().MakeServiceCall(ConstantUrl.URL+"signup.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("Status").equalsIgnoreCase("True")){
                    new CommonMethodClass(JsonSignupActivity.this,object.getString("Message"));
                    onBackPressed();
                }
                else{
                    new CommonMethodClass(JsonSignupActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}