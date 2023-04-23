package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MultipleListActivity extends AppCompatActivity {

    ListView listView;
    int iNumber;

    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_list);
        listView = findViewById(R.id.multiple_listview);
        Bundle bundle = getIntent().getExtras();
        iNumber = Integer.parseInt(bundle.getString("number"));

        arrayList = new ArrayList<>();
        for (int i = 0;i<iNumber;i++ ) {
            arrayList.add("Hello World "+(i+1));
        }
        ArrayAdapter adapter = new ArrayAdapter(MultipleListActivity.this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
    }
}