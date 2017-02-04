package com.example.tt.dao;

import android.content.Context;
import android.database.Cursor;

import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.NewsPic;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsPicDao extends BaseDao {
//    public NewsPicDao(Context context) {
//        super(context);
//    }

    public NewsPicDao(DatabaseHelp dbHelp) {
        super(dbHelp);
    }

    private NewsPic orm(Cursor cursor){
        NewsPic pic = new NewsPic();
        pic.setId(cursor.getInt(0));
        pic.setSrc(cursor.getInt(1));
        pic.setDesc(cursor.getString(2));
        return pic;
    }
    public NewsPic findById(Integer id){
        NewsPic pic = null;
        String sql = "select * from news_pic where id="+id;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            if(cursor.moveToNext()){
                pic = orm(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return pic;
    }
}
