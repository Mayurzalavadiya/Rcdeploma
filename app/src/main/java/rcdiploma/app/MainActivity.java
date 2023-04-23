package rcdiploma.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button b;
    EditText email,password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        b = findViewById(R.id.main_login_button);
        email = findViewById(R.id.main_email);
        password = findViewById(R.id.main_password);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equalsIgnoreCase("")){
                    email.setError("Email Id Required");
                }
                else if(!email.getText().toString().trim().matches(emailPattern)){
                    email.setError("Valid Email Id Required");
                }
                else if(password.getText().toString().trim().equalsIgnoreCase("")){
                    password.setError("Password Required");
                }
                else {
                    if(email.getText().toString().trim().equalsIgnoreCase("ADMIN@gmail.com") && password.getText().toString().trim().equals("Admin@007")){
                        System.out.println("Login Success");
                        Log.d("RCDEMO", "Login Successfully");
                        Log.e("RCDEMO", "Login Successfully");
                        //Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        new CommonMethodClass(MainActivity.this, "Login Successfully");
                        Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_SHORT).show();
                        new CommonMethodClass(view, "Login Successfully");
                        //new CommonMethodClass(MainActivity.this,HomeActivity.class);
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("emailKey",email.getText().toString());
                        bundle.putString("passwordKey",password.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{
                        new CommonMethodClass(MainActivity.this, "Login Unsuccessfully");
                        new CommonMethodClass(view, "Login Unsuccessfully");
                    }
                }
            }
        });

    }
}