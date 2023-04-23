package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class InputNumberActivity extends AppCompatActivity {

    EditText editText;
    Button submit;

    Spinner spinner;
    String[] technology = {"Android","Java","Flutter"};
    int[] images = {R.drawable.android_image,R.drawable.java,R.drawable.flutter_logo};

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_number);
        editText = findViewById(R.id.input_number_edit);
        submit = findViewById(R.id.input_number_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().trim().equalsIgnoreCase("")){
                    editText.setError("Number Required");
                }
                else{
                    Intent intent = new Intent(InputNumberActivity.this,MultipleListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("number",editText.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        imageView = findViewById(R.id.input_number_iv);
        spinner = findViewById(R.id.input_number_spinner);
        ArrayAdapter adapter = new ArrayAdapter(InputNumberActivity.this, android.R.layout.simple_list_item_1,technology);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                imageView.setImageResource(images[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}