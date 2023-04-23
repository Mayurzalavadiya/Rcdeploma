package rcdiploma.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SqliteListActivity extends AppCompatActivity {

    SQLiteDatabase db;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<String> arrayRollNoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_list);
        getSupportActionBar().hide();
        db = openOrCreateDatabase("student", MODE_PRIVATE, null);
        String tableQuery = "create table if not exists record(roll int(5),name varchar(255),mark int(3))";
        db.execSQL(tableQuery);

        listView = findViewById(R.id.sqlite_list);

        String selectQuery = "select * from record";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            arrayList = new ArrayList<>();
            arrayRollNoList = new ArrayList<>();
            do {
                arrayRollNoList.add(cursor.getString(0));
                arrayList.add(
                        "Roll No : " + cursor.getString(0)
                                + "\nName : " + cursor.getString(1)
                                + "\nMark : " + cursor.getString(2)
                );
            }
            while (cursor.moveToNext());
            ArrayAdapter adapter = new ArrayAdapter(SqliteListActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SqliteListActivity.this);
                    builder.setTitle("Delete Record!");
                    builder.setMessage("Are You Sure Want to delete Record?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String deleteQuery = "delete from record WHERE roll='"+arrayRollNoList.get(position)+"'";
                            db.execSQL(deleteQuery);
                            new CommonMethodClass(SqliteListActivity.this,"Delete Successfully");
                            arrayRollNoList.remove(position);
                            arrayList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                    return false;
                }
            });

        }
    }
}