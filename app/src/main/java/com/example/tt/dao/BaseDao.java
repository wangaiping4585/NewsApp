package com.example.tt.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tt.databasehelp.DatabaseHelp;

/**
 * Created by TT on 2016/9/18.
 */
public class BaseDao {
    private DatabaseHelp dbHelp;
    private SQLiteDatabase db;
    protected BaseDao(DatabaseHelp dbHelp) {
        //this.context = context;
//        dbHelp = new DatabaseHelp(context,"newsapp.db",1);
        this.dbHelp = dbHelp;
        db = dbHelp.getReadableDatabase();
    }

    protected void executeUpdate(String sql){
        db.execSQL(sql);
    }
    protected void executeUpdate(String sql, Object[] args){
        db.execSQL(sql,args);
    }
    protected Cursor executeQuery(String sql){
        return db.rawQuery(sql,null);
    }
    protected Cursor executeQuery(String sql,String[] args){
        return db.rawQuery(sql,args);
    }
    /**
     * GETTER AND SETTER
     * @return
     */
    protected DatabaseHelp getDbHelp() {
        return dbHelp;
    }
    protected void setDbHelp(DatabaseHelp dbHelp) {
        this.dbHelp = dbHelp;
    }
    public SQLiteDatabase getDb() {
        return db;
    }
    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
}
