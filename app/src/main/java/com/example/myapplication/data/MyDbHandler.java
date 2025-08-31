package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.MyListData;
import com.example.myapplication.model.Transaction;
import com.example.myapplication.params.Params;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + "(" + Params.KEY_ID + " INTEGER PRIMARY KEY,"
                + Params.KEY_REASON + " TEXT, " + Params.KEY_AMOUNT + " INTEGER, " + Params.KEY_DATE + " TEXT, " +
                Params.KEY_TIME + " TEXT,"+Params.KEY_TYPE+ " TEXT" + ")";
        Log.d("dbTransaction","Query being run is : "+ create);
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_REASON,transaction.getReason());
        values.put(Params.KEY_AMOUNT,transaction.getAmount());
        values.put(Params.KEY_DATE,transaction.getDate());
        values.put(Params.KEY_TIME,transaction.getTime());
        values.put(Params.KEY_TYPE,transaction.getType());

        db.insert(Params.TABLE_NAME,null,values);
        Log.d("dbTransaction","Successfully inserted");
        db.close();
    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //generate query to read from the database
        String select = "SELECT "+Params.KEY_REASON+","+Params.KEY_AMOUNT+","+Params.KEY_DATE+","+Params.KEY_TIME +" FROM " + Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);

        //loop through now
        if(cursor.moveToFirst()){
            do{
                Transaction transaction = new Transaction();
                transaction.setReason(cursor.getString(0));
                transaction.setAmount(cursor.getInt(1));
                transaction.setDate(cursor.getString(2));
                transaction.setTime(cursor.getString(3));
                transactionList.add(transaction);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }
    public int getSavingSum(){
        SQLiteDatabase db = this.getReadableDatabase();
        int sum = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + Params.KEY_AMOUNT + ") FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_TYPE + " = 's'", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }
        cursor.close();
        sum = sum - getExpenseSum();
        return sum;
    }
    public int getExpenseSum() {
        SQLiteDatabase db = this.getReadableDatabase();
        int sum = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + Params.KEY_AMOUNT + ") FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_TYPE + " = 'e'", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }
        cursor.close();
        return sum;
    }
    public  int getCategory(String str){
        SQLiteDatabase db = this.getReadableDatabase();
        int sum = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + Params.KEY_AMOUNT + ") FROM " + Params.TABLE_NAME + " WHERE " + Params.KEY_REASON + " LIKE '%" + str + "%'", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }
        cursor.close();
        return sum;
    }
}
