package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityToFragmentActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_fragment);
        button = findViewById(R.id.activity_fragment_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.activity_fragment_layout,new DemoFragment()).addToBackStack("").commit();
            }
        });

    }
}