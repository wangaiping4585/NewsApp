package com.example.tt.dao;

import android.content.Context;
import android.database.Cursor;

import com.example.tt.databasehelp.DatabaseHelp;
import com.example.tt.newsapp.R;
import com.example.tt.pojo.UserInfo;
import com.example.tt.util.Utils;

import java.util.Date;

/**
 * Created by TT on 2016/9/19.
 */
public class UserInfoDao extends BaseDao {
    public UserInfoDao(DatabaseHelp dbHelp) {
        super(dbHelp);
    }
    private UserInfo orm(Cursor cursor){
        UserInfo user = new UserInfo();
        user.setId(cursor.getInt(0));
        user.setUsername(cursor.getString(1));
        user.setPassword(cursor.getString(2));
        user.setNickname(cursor.getString(3));
        user.setSrc(cursor.getInt(4));
        user.setRegisterTime(cursor.getString(5));
        user.setCollectionCount(cursor.getInt(6));
        return user;
    }
    /**
     * 通过id查用户
     * @param id
     * @return
     */
    public UserInfo findById(Integer id){
        UserInfo user = null;
        String sql = "select * from user_info where id="+id;
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql);
            if(cursor.moveToNext()){
                user = orm(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return user;
    }

    /**
     * 用户登录验证
     * @param username
     * @param password
     * @return
     */
    public UserInfo findByUsernameAndPassword(String username,String password){
        UserInfo loginUser = null;
        String sql = "select * from user_info where username=? and password=?";
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql,new String[]{username,password});
            if(cursor.moveToNext()){
                loginUser = orm(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return loginUser;
    }
    /**
     * 用户名是否存在
     * @param username
     * @return
     */
    public boolean isUserExist(String username){
        int result = 0;
        String sql = "select id from user_info where username=?";
        Cursor cursor = null;
        try {
            cursor = executeQuery(sql,new String[]{username});
            if(cursor.moveToNext()){
                result = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result == 0 ? false : true;
    }
    /**
     * 保存用户
     * @param user
     */
    public void save(UserInfo user){
        String sql = "insert into user_info values(null,?,?,?,?,?,?)";
        executeUpdate(sql,new Object[]{
                user.getUsername(),
                user.getPassword(),
                user.getUsername(),
                R.drawable.doge,
                Utils.sdf.format(new Date()),
                0
        });
    }
    /**
     * 更新收藏数
     * @param userId
     * @param var
     */
    public void updateCollectionCount(Integer userId, int var){
        String sql = "update user_info set collection_count=collection_count+"+var+" where id="+userId;
        executeUpdate(sql);
    }
    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    public void updatePassword(Integer userId, String newPassword){
        String sql = "update user_info set password='"+newPassword+"' where id="+userId;
        executeUpdate(sql);
    }
    /**
     * 修改昵称
     * @param userId
     * @param newNickname
     */
    public void updateNickname(Integer userId, String newNickname){
        String sql = "update user_info set nickname='"+newNickname+"' where id="+userId;
        executeUpdate(sql);
    }
}
