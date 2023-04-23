package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BranchListActivity extends AppCompatActivity {

    ListView listView;
    String[] branchArray = {"IT","CE","ME","Civil Engineering"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_list);
        listView = findViewById(R.id.branch_listview);
        ArrayAdapter adapter = new ArrayAdapter(BranchListActivity.this, android.R.layout.simple_list_item_1,branchArray);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CommonMethodClass(BranchListActivity.this,SemesterExpandActivity.class);
            }
        });

    }
}