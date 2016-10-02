package com.example.happy.primeno;

/**
 * Created by Happy on 10/1/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.*;
public class PrimeNoDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PrimeNo.db";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + PrimeNoSchema.RecordTable.TABLENAME + " (" +
                    PrimeNoSchema.RecordTable.COLUMN_NAME_ID + " text PRIMARY KEY," +
                    PrimeNoSchema.RecordTable.COLUMN_NAME_TOTAL_QUESTIONS + " INTEGER , " +
                    PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS + " INTEGER , " +
                    PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_INCORRECT_QUESTIONS + " INTEGER " + " )";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + PrimeNoSchema.RecordTable.TABLENAME;

    public PrimeNoDB(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
