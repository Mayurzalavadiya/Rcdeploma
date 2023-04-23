package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class SqliteListviewActivity extends AppCompatActivity {

    SearchView searchView;
    ListView listView;
    ArrayList<SqliteDataList> arrayList;
    SqliteListviewAdapter adapter;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_listview);
        listView = findViewById(R.id.sqlite_listview);
        searchView = findViewById(R.id.sqlite_listview_searchview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().equalsIgnoreCase("")){
                    adapter.filter("");
                }
                else{
                    adapter.filter(newText);
                }
                return false;
            }
        });

        db = openOrCreateDatabase("student", MODE_PRIVATE, null);
        String tableQuery = "create table if not exists record(roll int(5),name varchar(255),mark int(3))";
        db.execSQL(tableQuery);

        String selectQuery = "select * from record";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            arrayList = new ArrayList<>();
            do {
                SqliteDataList list = new SqliteDataList();
                list.setRoll(cursor.getString(0));
                list.setName(cursor.getString(1));
                list.setMark(cursor.getString(2));
                arrayList.add(list);
            }
            while (cursor.moveToNext());
            adapter = new SqliteListviewAdapter(SqliteListviewActivity.this, arrayList);
            listView.setAdapter(adapter);
        }
    }

    private class SqliteListviewAdapter extends BaseAdapter {

        Context context;
        ArrayList<SqliteDataList> arrayList;
        ArrayList<SqliteDataList> searchList;

        public SqliteListviewAdapter(SqliteListviewActivity sqliteListviewActivity, ArrayList<SqliteDataList> arrayList) {
            this.context = sqliteListviewActivity;
            this.arrayList = arrayList;
            this.searchList = new ArrayList<>();
            searchList.addAll(arrayList);
        }

        public void filter(String s){
            s = s.toLowerCase(Locale.getDefault());
            arrayList.clear();
            if(s.length()==0){
                arrayList.addAll(searchList);
            }
            else{
                for(SqliteDataList l : searchList){
                    if(l.getRoll().toLowerCase(Locale.getDefault()).contains(s) || l.getMark().toLowerCase(Locale.getDefault()).contains(s) || l.getName().toLowerCase(Locale.getDefault()).contains(s)){
                        arrayList.add(l);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_sqlite_list,null);
            TextView rollNo = view.findViewById(R.id.custom_sqlite_list_roll);
            TextView name = view.findViewById(R.id.custom_sqlite_list_name);
            TextView mark = view.findViewById(R.id.custom_sqlite_list_mark);
            ImageView delete = view.findViewById(R.id.custom_sqlite_list_delete);

            rollNo.setText(arrayList.get(i).getRoll());
            name.setText(arrayList.get(i).getName());
            mark.setText(arrayList.get(i).getMark());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String deleteQuery = "delete from record WHERE roll='"+arrayList.get(i).getRoll()+"'";
                    db.execSQL(deleteQuery);
                    new CommonMethodClass(context,"Delete Successfully");
                    arrayList.remove(i);
                    adapter.notifyDataSetChanged();
                }
            });

            return view;
        }
    }
}