package com.example.tt.dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.NewsInfo;
import com.example.tt.pojo.NewsType;
import com.example.tt.pojo.UserInfo;
import com.example.tt.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsInfoDao extends BaseDao {
    public NewsInfoDao(DatabaseHelp dbHelp) {
        super(dbHelp);
    }

    private NewsInfo orm(Cursor cursor){
        NewsInfo news = new NewsInfo();
        news.setId(cursor.getInt(0));
        news.setTitle(cursor.getString(1));
        news.setContent(cursor.getString(2));
        news.setNewsType(new NewsTypeDao(getDbHelp()).findById(cursor.getInt(3)));
        news.setPubTime(cursor.getString(4));
        news.setPubIns(cursor.getString(5));
        news.setCommentCount(cursor.getInt(6));
        news.setNewsPic(new NewsPicDao(getDbHelp()).findById(cursor.getInt(7)));
        news.setNewsPic2(new NewsPicDao(getDbHelp()).findById(cursor.getInt(8)));
        news.setNewsPic3(new NewsPicDao(getDbHelp()).findById(cursor.getInt(9)));
        news.setType(cursor.getInt(10));
        news.setHasCollected(this.hasCollected(news.getId(), Utils.loginUser==null?0:Utils.loginUser.getId()));
        return news;
    }
    /**
     * @return 返回所有新闻
     */
    public List<NewsInfo> findAll(){
        List<NewsInfo> list = new ArrayList<>();
        String sql = "select * from news_info order by pub_time desc";
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            while(cursor.moveToNext()){
                NewsInfo news = orm(cursor);
                list.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
    /**
     * 根据类别查找新闻
     * @return
     */
    public List<NewsInfo> findByNewsType(Integer newsType){
        List<NewsInfo> list = new ArrayList<>();
        String sql = "select * from news_info where news_type="+newsType+" order by pub_time desc";
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            while(cursor.moveToNext()){
                NewsInfo news = orm(cursor);
                list.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
    /**
     * 按id查找新闻
     * @param id
     * @return
     */
    public NewsInfo findById(Integer id){
        NewsInfo news = null;
        String sql = "select * from news_info where id="+id;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            if(cursor.moveToNext()){
                news = orm(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return news;
    }
    public void updateCommentCount(NewsInfo news){
        String sql = "update news_info set comment_count=? where id=?";
        executeUpdate(sql,new Object[]{news.getCommentCount()+1,news.getId()});
    }

    /**
     * 更新收藏状态
     * @param newsId
     * @param userId
     * @param var
     */
    private void updateCollected(Integer newsId, Integer userId,int var){
        String sql = "select id from user_collect_news" +
                " where user_info="+userId+
                " and news_info="+newsId;
        Cursor cursor = executeQuery(sql);
        if(cursor.moveToNext()){
            String sql1 = "update user_collect_news set has_collected="+var +
                    " where id="+cursor.getInt(0);
            executeUpdate(sql1);
        }else{
            String sql2 = "insert into user_collect_news values(null,"+userId+","+newsId+","+var+")";
            executeUpdate(sql2);
        }
        cursor.close();
    }
    public void collected(Integer newsId, Integer userId){
        updateCollected(newsId, userId, 1);
    }
    public void disCollected(Integer newsId, Integer userId){
        updateCollected(newsId, userId, 0);
    }
    /**
     * 用户是否收藏新闻
     * @param newsId
     * @param userId
     * @return
     */
    public int hasCollected(Integer newsId, Integer userId){
        int result = 0;
        String sql = "select id from user_collect_news" +
                " where has_collected=1" +
                " and user_info="+userId+"" +
                " and news_info="+newsId;
        Cursor cursor = executeQuery(sql);
        if(cursor.moveToNext()){
            result = 1;
        }
        cursor.close();
        return result;
    }
    /**
     * 查找用户收藏的新闻
     * @param userId
     * @return
     */
    public List<NewsInfo> findByUserCollection(Integer userId){
        String sql = "select n.* from news_info n, user_collect_news ucn" +
                " where n.id=ucn.news_info" +
                " and has_collected=1" +
                " and ucn.user_info="+userId+" order by ucn.id desc";
        List<NewsInfo> list = new ArrayList<>();
        Cursor cursor = executeQuery(sql);
        while (cursor.moveToNext()){
            NewsInfo news = orm(cursor);
            list.add(news);
        }
        cursor.close();
        return list;
    }
}
