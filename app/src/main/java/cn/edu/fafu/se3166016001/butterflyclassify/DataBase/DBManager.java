package cn.edu.fafu.se3166016001.butterflyclassify.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;

public class DBManager {
    private static  DBManager instance = null;
    private static SQLiteDatabase database;
    private DBManager(SQLiteDatabase db){ database = db; }
    public static DBManager getInstance(SQLiteDatabase db) {
        if(instance == null) {
            instance = new DBManager(db);
        }
        return instance;
    }


    public  boolean insertUser(User user){
        ContentValues values = new ContentValues();
        values.put("name",user.getUserName());
        values.put("token",user.getToken());
        long rowId = database.insert("user",null,values);
        return rowId > 0;
    }

    public  User queryLastUser(){
        Cursor cursor = database.query("user",new String[] {"name","token"},
                null,null,null,null,
                "id DESC", "1");
        boolean succeed = cursor.moveToFirst();
        User user = null;
        if(succeed) {
            user = new User();
            user.setUserName(cursor.getString(cursor.getColumnIndex("name")));
            user.setToken(cursor.getString(cursor.getColumnIndex("token")));
        }
        return user;
    }

    public  int updateUserToken(User user){
        ContentValues values = new ContentValues();
        values.put("token",user.getToken());
        return database.update("user",values,"name=?",new String[]{user.getUserName()});
    }

    public void deleteAll() {
        database.delete("user",null,null);
    }

}
