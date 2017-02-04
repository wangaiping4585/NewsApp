package com.example.tt.dao;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.pojo.Comment;
import com.example.tt.pojo.UserInfo;
import com.example.tt.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TT on 2016/9/19.
 */
public class CommentDao extends BaseDao {
    public CommentDao(DatabaseHelp dbHelp) {
        super(dbHelp);
    }
    private Comment orm(Cursor cursor){
        Comment c = new Comment();
        c.setId(cursor.getInt(0));
        c.setContent(cursor.getString(1));
        c.setNewsInfo(new NewsInfoDao(getDbHelp()).findById(cursor.getInt(2)));
        c.setComment(this.findById(cursor.getInt(3)));
        c.setUserInfo(new UserInfoDao(getDbHelp()).findById(cursor.getInt(4)));
        c.setCommentTime(cursor.getString(5));
        c.setUpCount(cursor.getInt(6));
        c.setIsUp(this.hasLiked(c,Utils.loginUser==null?0:Utils.loginUser.getId()));
        return c;
    }
    /**
     * 分页,通过新闻id找评论,按时间降序
     * @param newsId
     * @return
     */
    public List<Comment> findByNewsIdWithPagination(Integer newsId, int pid){
        List<Comment> list = new ArrayList<>();
        String sql = "select *" +
                " from comment" +
                " where news_info=" + newsId +
                " order by comment_time desc" +
                " limit "+(pid-1)*Utils.commentPageSize+","+Utils.commentPageSize;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            while(cursor.moveToNext()){
                Comment c = orm(cursor);
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
    /**
     * 通过新闻id和topNum找点赞数最多的评论
     * @param newsId
     * @param topNum
     * @return
     */
    public List<Comment> findByNewsIdAndTopNum(Integer newsId, Integer topNum){
        List<Comment> list = new ArrayList<>();
        String sql = "select * from comment" +
                " where news_info="+newsId+"" +
                " order by up_count desc" +
                " limit 0,"+topNum;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            while(cursor.moveToNext()){
                Comment c = orm(cursor);
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }
    /**
     * 通过id找评论
     * @param id
     * @return
     */
    public Comment findById(Integer id){
        Comment comment = null;
        String sql = "select * from comment where id="+id;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            if(cursor.moveToNext()){
                comment = orm(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return comment;
    }
    /**
     * @param comment 添加评论
     */
    public void save(Comment comment){
        String sql = "insert into comment values(null,?,?,?,?,?,0)";
        Object[] args = {comment.getContent(),
                comment.getNewsInfo().getId(),
                comment.getComment().getId(),
                comment.getUserInfo().getId(),
                comment.getCommentTime()};
        executeUpdate(sql, args);
    }

    /**
     * 是否点过赞
     * @param comment
     * @param userId
     * @return
     */
    public int hasLiked (Comment comment, Integer userId){
        int hasLiked = 0;
        String sql = "select id from user_up_comment" +
                " where comment="+comment.getId()+" and user_info="+userId+" and is_up=1";
        Cursor c = executeQuery(sql);
        if(c.moveToNext()){
            hasLiked = 1;
        }
        c.close();
        return hasLiked;
    }
    /**
     * 点赞
     * @param comment
     * @param userId
     * @param isUp
     */
    public void like(Comment comment, Integer userId,int isUp){
        String sql = "select id from user_up_comment" +
                " where comment="+comment.getId()+" and user_info="+userId;
        Cursor c = executeQuery(sql);
        if(c.moveToNext()){
            String sql1 = "update user_up_comment set is_up =?" +
                    " where comment=?" +
                    " and user_info=?";
            executeUpdate(sql1,new Object[]{isUp,comment.getId(),userId});
        }else{
            String sql2 = "insert into user_up_comment values(null, ?, ?, ?)";
            executeUpdate(sql2,new Object[]{userId,comment.getId(),isUp});
        }
        c.close();
    }
    /**
     * 更新点赞数
     */
    public void updateUpCount(Integer commentId,int var){
        String sql = "update comment set up_count=up_count+"+var+" where id="+commentId;
        executeUpdate(sql);
    }
}
