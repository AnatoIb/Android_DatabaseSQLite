package com.example.lab5db;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "workers.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "users";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_MANAGER = "manager";
    static final String COLUMN_SALARY = "salary";
    static final String COLUMN_UNIT_NUM = "unit_num";
    static final String COLUMN_NAME_OF_UNIT = "name_of_unit";
    static final String COLUMN_CITY = "city";
    static final String COLUMN_ETC = "etc";

    private Context context;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.context = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    void createDB(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            try(InputStream myInput = context.getAssets().open(DB_NAME);

                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}