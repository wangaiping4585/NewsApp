package com.example.tt.dao;

import android.content.Context;
import android.database.Cursor;

import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.NewsType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsTypeDao extends BaseDao {
//    public NewsTypeDao(Context context) {
//        super(context);
//    }

    public NewsTypeDao(DatabaseHelp dbHelp) {
        super(dbHelp);
    }

    private NewsType orm(Cursor cursor){
        NewsType nt = new NewsType();
        nt.setId(cursor.getInt(0));
        nt.setName(cursor.getString(1));
        return nt;
    }
    /**
     * @return 返回所有类型
     */
    public List<NewsType> findAll(){
        List<NewsType> list = new ArrayList<>();
        String sql = "select * from news_type";
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            while(cursor.moveToNext()){
                NewsType nt = orm(cursor);
                list.add(nt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
    /**
     * 按id查找类型
     * @param id
     * @return
     */
    public NewsType findById(Integer id){
        NewsType nt = null;
        String sql = "select * from news_type where id="+id;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            while(cursor.moveToNext()){
                nt = orm(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return nt;
    }
}
