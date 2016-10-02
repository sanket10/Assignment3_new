package com.example.happy.primeno;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Happy on 10/1/2016.
 */
public class PrimeNoDAO extends PrimeNoDB{
    public static int best_no_of_correct;
    public static int worst_no_of_correct;
    public static String tmp;
    public PrimeNoDAO(Context context){
        super(context);
    }
    public long insertRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PrimeNoSchema.RecordTable.COLUMN_NAME_ID,record.getCreate_date());
        contentValues.put(PrimeNoSchema.RecordTable.COLUMN_NAME_TOTAL_QUESTIONS,record.getTotal_no_of_questions());
        contentValues.put(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS,record.getTotal_no_of_correct_questions());
        contentValues.put(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_INCORRECT_QUESTIONS,record.getTotal_no_of_incorrect_questions());
        long rs = db.insert(PrimeNoSchema.RecordTable.TABLENAME,null,contentValues);
        db.close();
        return rs;
    }

    public List<Record> getAllRecord(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Record> records = new LinkedList<Record>();

        Cursor cursor = db.rawQuery("SELECT * FROM record",null);
        while(cursor.moveToNext()){
            Record tmp = new Record();
            tmp.setCreate_date(cursor.getString(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_ID)));
            tmp.setTotal_no_of_questions(cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_TOTAL_QUESTIONS)));
            tmp.setTotal_no_of_correct_questions(cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS)));
            tmp.setTotal_no_of_incorrect_questions(cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_INCORRECT_QUESTIONS)));
            records.add(tmp);
        }
        db.close();
        cursor.close();
        return records;
    }

    public double getStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        this.best_no_of_correct = 0;
        this.worst_no_of_correct = 999;
        //db.rawQuery("insert into record values('dkdd',3,1,2)",null);
        Cursor cursor = db.rawQuery("SELECT no_of_question,no_of_correct_question FROM record",null);
        cursor.moveToFirst();
        if(cursor.isAfterLast()){
            return 0;
        }
        int no_of_question = cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_TOTAL_QUESTIONS));
        int no_of_correct_question = cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS));
        this.best_no_of_correct = no_of_correct_question;
        this.worst_no_of_correct = no_of_question;
        while(cursor.moveToNext()){
            no_of_question += cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_TOTAL_QUESTIONS));
            no_of_correct_question += cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS));
            if(this.best_no_of_correct < cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS))){
                this.best_no_of_correct = cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS));
            }
            if(this.worst_no_of_correct > cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS))){
                this.worst_no_of_correct = cursor.getInt(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_NO_OF_CORRECT_QUESTIONS));
            }
        }
        if(no_of_question == 0){
            no_of_question = 1;
        }
        db.close();
        cursor.close();
        return (no_of_correct_question * 100)/no_of_question;
    }

    public int getBest_no_of_correct(){
        return this.best_no_of_correct;
    }

    public boolean improveStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT rid FROM record WHERE no_of_correct_question = "+this.worst_no_of_correct ,null);
        cursor.moveToFirst();
        if(cursor.isAfterLast()){
            return false;
        }
        String rid = cursor.getString(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_ID));
        db.delete("record","rid = ?",new String[]{rid});
        return true;
    }
    //update best performance oncheat button click
    public boolean updateStatus(){
        Log.d("PRIMEDAO","enter updatestatus");
        SQLiteDatabase db = this.getWritableDatabase();
        int aa = this.best_no_of_correct - 2;
        Cursor cursor = db.rawQuery("select rid from record where no_of_correct_question = "+this.best_no_of_correct,null);
        cursor.moveToFirst();
        if(cursor.isAfterLast()){
            PrimeNoDAO.tmp = "No Record";
            return false;
        }
        String rid = cursor.getString(cursor.getColumnIndex(PrimeNoSchema.RecordTable.COLUMN_NAME_ID));

        db.execSQL("UPDATE record SET no_of_correct_question = ? WHERE no_of_correct_question = " + this.best_no_of_correct,
                new String[] {
                    aa+""
                });

        Log.d("PRIMEDAO","return updatestatus");
        return true;
    }
}
