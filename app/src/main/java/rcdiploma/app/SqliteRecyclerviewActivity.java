package rcdiploma.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class SqliteRecyclerviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SqliteDataList> arrayList;
    SqliteDataAdapter adapter;

    SQLiteDatabase db;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_recyclerview);
        recyclerView = findViewById(R.id.sqlite_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(SqliteRecyclerviewActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView = findViewById(R.id.sqlite_searchview);

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
            adapter = new SqliteDataAdapter(SqliteRecyclerviewActivity.this, arrayList);
            recyclerView.setAdapter(adapter);
        }

    }

    private class SqliteDataAdapter extends RecyclerView.Adapter<SqliteDataAdapter.MyHolder> {

        Context context;
        ArrayList<SqliteDataList> arrayList;
        ArrayList<SqliteDataList> searchList;

        public SqliteDataAdapter(SqliteRecyclerviewActivity sqliteRecyclerviewActivity, ArrayList<SqliteDataList> arrayList) {
            this.context = sqliteRecyclerviewActivity;
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

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sqlite_recycler, parent, false);
            return new MyHolder(view);
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            TextView roll, name, mark;
            ImageView deleteIv;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                roll = itemView.findViewById(R.id.custom_sqlite_recycler_roll);
                name = itemView.findViewById(R.id.custom_sqlite_recycler_name);
                mark = itemView.findViewById(R.id.custom_sqlite_recycler_mark);
                deleteIv = itemView.findViewById(R.id.custom_sqlite_recycler_delete);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.roll.setText(arrayList.get(position).getRoll());
            holder.name.setText(arrayList.get(position).getName());
            holder.mark.setText(arrayList.get(position).getMark());

            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String deleteQuery = "delete from record WHERE roll='"+arrayList.get(position).getRoll()+"'";
                    db.execSQL(deleteQuery);
                    new CommonMethodClass(context,"Delete Successfully");
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}