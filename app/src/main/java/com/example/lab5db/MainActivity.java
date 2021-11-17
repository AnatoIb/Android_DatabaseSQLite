package com.example.lab5db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    EditText searchEditText;
    String[] headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = findViewById(R.id.listView);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.createDB();
    }

    @Override
    public void onResume() {
        super.onResume();

        db = databaseHelper.open();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
        headers = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_MANAGER,
                DatabaseHelper.COLUMN_SALARY, DatabaseHelper.COLUMN_UNIT_NUM,
                DatabaseHelper.COLUMN_NAME_OF_UNIT, DatabaseHelper.COLUMN_CITY,
                DatabaseHelper.COLUMN_ETC};

        userAdapter = new SimpleCursorAdapter(this, R.layout.workers_item,
                userCursor, headers, new int[]{R.id.worker_id, R.id.name, R.id.manager, R.id.salary,
                R.id.unit_num, R.id.name_of_unit, R.id.city, R.id.etc}, 0);

        userList.setAdapter(userAdapter);
    }

    public void searchClick(View view) {
        searchEditText = findViewById(R.id.searchEditText);
        String searchValue = searchEditText.getText().toString();

        if (searchValue.length() == 0)
            onResume();
        else {
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where _id = " + Integer.parseInt(searchValue), null);

            userAdapter = new SimpleCursorAdapter(this, R.layout.workers_item,
                    userCursor, headers, new int[]{R.id.worker_id, R.id.name, R.id.manager, R.id.salary,
                    R.id.unit_num, R.id.name_of_unit, R.id.city, R.id.etc}, 0);

            userList.setAdapter(userAdapter);
        }
    }

    public void add(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        db.close();
        userCursor.close();
    }
}