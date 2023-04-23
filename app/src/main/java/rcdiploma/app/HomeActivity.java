package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView email, password;
    //RadioButton male,female,transgender;
    RadioGroup gender;

    CheckBox androidCheck,ios,flutter,reactnative;
    Button showTechnology;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email = findViewById(R.id.home_email);
        password = findViewById(R.id.home_password);

        Bundle bundle=getIntent().getExtras();
        email.setText(bundle.getString("emailKey"));
        password.setText(bundle.getString("passwordKey"));

        gender = findViewById(R.id.home_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = gender.getCheckedRadioButtonId();
                RadioButton rb = findViewById(id);
                new CommonMethodClass(HomeActivity.this,rb.getText().toString());
            }
        });

        /*male = findViewById(R.id.home_male);
        female = findViewById(R.id.home_female);
        transgender = findViewById(R.id.home_transgender);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(HomeActivity.this,male.getText().toString());
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(HomeActivity.this,female.getText().toString());
            }
        });

        transgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(HomeActivity.this,transgender.getText().toString());
            }
        });*/

        androidCheck = findViewById(R.id.home_android);
        ios = findViewById(R.id.home_ios);
        flutter = findViewById(R.id.home_flutter);
        reactnative = findViewById(R.id.home_react_native);
        showTechnology = findViewById(R.id.home_show_technology);

        showTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                if(androidCheck.isChecked()){
                    //new CommonMethodClass(HomeActivity.this,androidCheck.getText().toString());
                    sb.append(androidCheck.getText().toString()+"\n");
                }
                if(ios.isChecked()){
                    //new CommonMethodClass(HomeActivity.this,ios.getText().toString());
                    sb.append(ios.getText().toString()+"\n");
                }
                if(flutter.isChecked()){
                    sb.append(flutter.getText().toString()+"\n");
                    //new CommonMethodClass(HomeActivity.this,flutter.getText().toString());
                }
                if(reactnative.isChecked()){
                    sb.append(reactnative.getText().toString()+"\n");
                    //new CommonMethodClass(HomeActivity.this,reactnative.getText().toString());
                }
                new CommonMethodClass(HomeActivity.this,sb.toString());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            //onBackPressed();
            alertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        alertDialog();
        //super.onBackPressed();
    }

    private void alertDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Are You Sure Want To Exit!!!");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new CommonMethodClass(HomeActivity.this,"Rate Us");
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}