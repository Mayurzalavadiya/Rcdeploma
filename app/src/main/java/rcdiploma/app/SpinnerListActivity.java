package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SpinnerListActivity extends AppCompatActivity {

    Spinner spinner;
    String[] technology = {"Android","Java","Php","IOS","Flutter","React Native","Test Technology"};
    ArrayList<String> arrayList;

    GridView listView;

    String[] listViewTechnology = {"Android","Java","Php","IOS","Flutter","React Native","Python","Android","Java","Php","IOS","Flutter","React Native","Python","Android","Java","Php","IOS","Flutter","React Native","Android","Java","Php","IOS","Flutter","React Native"};
    ArrayList<String> listviewArrayList;

    AutoCompleteTextView autoCompleteTextView;
    MultiAutoCompleteTextView multiautoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_list);
        spinner = findViewById(R.id.spinner);

        arrayList = new ArrayList<>();
        /*arrayList.add("Android");
        arrayList.add("Java");
        arrayList.add("Php");
        arrayList.add("IOS");
        arrayList.add("Flutter");
        arrayList.add("React Native");*/
        for(int i=0;i<technology.length;i++){
            arrayList.add(technology[i]);
        }
        arrayList.set(2,"Core Php");
        arrayList.remove(6);

        ArrayAdapter adapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new CommonMethodClass(SpinnerListActivity.this,arrayList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView = findViewById(R.id.spinner_listview);
        listviewArrayList = new ArrayList<>();
        for(int i=0;i<listViewTechnology.length;i++){
            listviewArrayList.add(listViewTechnology[i]);
        }
        ArrayAdapter listadapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,listviewArrayList);
        listView.setAdapter(listadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CommonMethodClass(SpinnerListActivity.this,listviewArrayList.get(i));
            }
        });

        autoCompleteTextView = findViewById(R.id.spinner_autocomplete);
        ArrayAdapter autoadapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,listviewArrayList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoadapter);

        multiautoCompleteTextView = findViewById(R.id.spinner_multiautocomplete);
        ArrayAdapter multiautoadapter = new ArrayAdapter(SpinnerListActivity.this, android.R.layout.simple_list_item_1,listviewArrayList);
        multiautoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiautoCompleteTextView.setThreshold(1);
        multiautoCompleteTextView.setAdapter(multiautoadapter);

    }
}