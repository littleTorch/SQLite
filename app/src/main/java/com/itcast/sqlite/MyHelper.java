package com.itcast.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyHelper extends SQLiteOpenHelper {
    private static Context context;
    public MyHelper(Context context){
        super(context,"abc.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE information(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(20), phone VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public void add(Context context,String name,String phone){
        MyHelper myHelper=new MyHelper(context);
        ContentValues values;
        SQLiteDatabase db=myHelper.getWritableDatabase();
        values =new ContentValues();
        values.put("name",name);
        values.put("phone",phone);
        db.insert("information",null,values);
        db.close();
    }
    public List<NameAndPhone> qurey(Context context){
        SQLiteDatabase db;
        MyHelper myHelper=new MyHelper(context);
        db=myHelper.getReadableDatabase();
        List<NameAndPhone> nameAndPhones=new ArrayList<>();
        Cursor cursor=db.query("information",null,null,null,null,null,null);
        if (cursor.getCount()==0) {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
            NameAndPhone n=new NameAndPhone();
            n.setName(cursor.getString(cursor.getColumnIndex("name")));
            n.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            nameAndPhones.add(n);
            }
        }
        cursor.close();
        return nameAndPhones;
    }
    public void update(Context context,String phone,String name){
        SQLiteDatabase db;
        ContentValues values;
        MyHelper myHelper=new MyHelper(context);
        db=myHelper.getWritableDatabase();
        values =new ContentValues();
        values.put("phone",phone);
        db.update("information",values,"name=?",new String[]{name});
        db.close();
    }
    public void detete(Context context){
        SQLiteDatabase db;
        MyHelper myHelper=new MyHelper(context);
        db=myHelper.getWritableDatabase();
        db.delete("information",null,null);
        db.close();
    }
}
