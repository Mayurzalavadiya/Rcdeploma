package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SqliteActivity extends AppCompatActivity {

    EditText rollEdit, nameEdit, markEdit;
    Button insertButton, updateButton, deleteButton, showButton,showRecyclerButton,showCustomListButton, searchButton;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        getSupportActionBar().hide();
        db = openOrCreateDatabase("student", MODE_PRIVATE, null);
        String tableQuery = "create table if not exists record(roll int(5),name varchar(255),mark int(3))";
        db.execSQL(tableQuery);
        rollEdit = findViewById(R.id.sqlite_roll);
        nameEdit = findViewById(R.id.sqlite_name);
        markEdit = findViewById(R.id.sqlite_mark);
        insertButton = findViewById(R.id.sqlite_insert);
        updateButton = findViewById(R.id.sqlite_update);
        deleteButton = findViewById(R.id.sqlite_delete);
        showButton = findViewById(R.id.sqlite_show);
        showRecyclerButton = findViewById(R.id.sqlite_show_recycler);
        showCustomListButton = findViewById(R.id.sqlite_show_custom_list);

        searchButton = findViewById(R.id.sqlite_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rollEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    rollEdit.setError("Roll No Required");
                } else {
                    String searchQuery = "select * from record where roll='" + rollEdit.getText().toString() + "'";
                    Cursor cursor = db.rawQuery(searchQuery, null);
                    if (cursor.moveToFirst()) {
                        do {
                            rollEdit.setText(cursor.getString(0));
                            nameEdit.setText(cursor.getString(1));
                            markEdit.setText(cursor.getString(2));
                        }
                        while (cursor.moveToNext());
                    }
                }
            }
        });

        showCustomListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(SqliteActivity.this, SqliteListviewActivity.class);
            }
        });

        showRecyclerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(SqliteActivity.this, SqliteRecyclerviewActivity.class);
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethodClass(SqliteActivity.this, SqliteListActivity.class);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rollEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    rollEdit.setError("Roll No. Required");
                } else {
                    String checkDataQuery = "select * from record WHERE roll='"+rollEdit.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(checkDataQuery,null);
                    if(cursor.getCount()>0){
                        String deleteQuery = "delete from record WHERE roll='" + rollEdit.getText().toString() + "'";
                        db.execSQL(deleteQuery);
                        new CommonMethodClass(SqliteActivity.this, "Delete Successfully");
                        clearData();
                    }
                    else{
                        new CommonMethodClass(SqliteActivity.this,"Record Not Found");
                    }
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rollEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    rollEdit.setError("Roll No Required");
                } else if (nameEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    nameEdit.setError("Name Required");
                } else if (markEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    markEdit.setError("Mark Required");
                } else {
                    String checkDataQuery = "select * from record WHERE roll='"+rollEdit.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(checkDataQuery,null);
                    if(cursor.getCount()>0){
                        String updateQuery = "update record set name='" + nameEdit.getText().toString() + "',mark='" + markEdit.getText().toString() + "' WHERE roll='" + rollEdit.getText().toString() + "'";
                        db.execSQL(updateQuery);
                        new CommonMethodClass(SqliteActivity.this, "Update Successfully");
                        clearData();
                    }
                    else {
                        new CommonMethodClass(SqliteActivity.this,"Record Not Found");
                    }
                }
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rollEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    rollEdit.setError("Roll No Required");
                } else if (nameEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    nameEdit.setError("Name Required");
                } else if (markEdit.getText().toString().trim().equalsIgnoreCase("")) {
                    markEdit.setError("Mark Required");
                } else {
                    String checkDataQuery = "select * from record WHERE roll='"+rollEdit.getText().toString()+"' AND name='"+nameEdit.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(checkDataQuery,null);
                    if(cursor.getCount()>0){
                        new CommonMethodClass(SqliteActivity.this,"Record Already Exists");
                    }
                    else {
                        String insertQuery = "insert into record VALUES('" + rollEdit.getText().toString() + "','" + nameEdit.getText().toString() + "','" + markEdit.getText().toString() + "')";
                        db.execSQL(insertQuery);
                        new CommonMethodClass(SqliteActivity.this, "Insert Successfully");
                        clearData();
                    }
                }
            }
        });

    }

    private void clearData() {
        rollEdit.setText("");
        nameEdit.setText("");
        markEdit.setText("");
        rollEdit.requestFocus();
    }

}