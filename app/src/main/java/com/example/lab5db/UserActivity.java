package com.example.lab5db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    EditText editId;
    EditText editName;
    EditText editManager;
    EditText editSalary;
    EditText editUnitNum;
    EditText editNameOfUnit;
    EditText editCity;
    EditText editETC;

    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        editId = findViewById(R.id.editId);
        editName = findViewById(R.id.editName);
        editManager = findViewById(R.id.editManager);
        editSalary = findViewById(R.id.editSalary);
        editUnitNum = findViewById(R.id.editUnitNum);
        editNameOfUnit = findViewById(R.id.editNameOfUnit);
        editCity = findViewById(R.id.editCity);
        editETC = findViewById(R.id.editETC);
        
        delButton = findViewById(R.id.delete);
        saveButton = findViewById(R.id.save);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();

            editId.setText(String.valueOf(userCursor.getInt(0)));
            editName.setText(userCursor.getString(1));
            editManager.setText(userCursor.getString(2));
            editSalary.setText(String.valueOf(userCursor.getInt(3)));
            editUnitNum.setText(String.valueOf(userCursor.getInt(4)));
            editNameOfUnit.setText(userCursor.getString(5));
            editCity.setText(userCursor.getString(6));
            editETC.setText(String.valueOf(userCursor.getInt(7)));

            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }
    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ID, Integer.parseInt(editId.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_NAME, editName.getText().toString());
        cv.put(DatabaseHelper.COLUMN_MANAGER, editManager.getText().toString());
        cv.put(DatabaseHelper.COLUMN_SALARY, Integer.parseInt(editSalary.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_UNIT_NUM, Integer.parseInt(editUnitNum.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_NAME_OF_UNIT, editNameOfUnit.getText().toString());
        cv.put(DatabaseHelper.COLUMN_CITY, editCity.getText().toString());
        cv.put(DatabaseHelper.COLUMN_ETC, Integer.parseInt(editETC.getText().toString()));

        if (userId > 0) {
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + userId, null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){

        db.close();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}