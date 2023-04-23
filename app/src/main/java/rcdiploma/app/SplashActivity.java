package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        sp = getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);
        imageView = findViewById(R.id.splash_iv);

        AlphaAnimation animation = new AlphaAnimation(0,1);
        //animation.setRepeatCount(3);
        animation.setDuration(2700);
        imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*new CommonMethodClass(SplashActivity.this,TabDemoActivity.class);
                finish();*/
                if(sp.getString(ConstantUrl.ID,"").equalsIgnoreCase("")){
                    new CommonMethodClass(SplashActivity.this,JsonLoginActivity.class);
                    finish();
                }
                else{
                    new CommonMethodClass(SplashActivity.this,CategoryActivity.class);
                    //new CommonMethodClass(SplashActivity.this,JsonProfileActivity.class);
                    finish();
                }
            }
        }, 3000);

    }
}