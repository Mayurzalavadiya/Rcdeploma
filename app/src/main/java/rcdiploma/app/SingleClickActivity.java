package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SingleClickActivity extends AppCompatActivity {

    TextView textView;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_click);
        textView = findViewById(R.id.single_click_text);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter==0){
                    new CommonMethodClass(SingleClickActivity.this,"Hello");
                    counter++;
                }
            }
        });

    }
}