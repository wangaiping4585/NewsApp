package com.example.tt.pojo;

import java.io.Serializable;

/**
 * Created by TT on 2016/9/18.
 */
public class Comment implements Serializable {
    private Integer id;
    private String content;
    private NewsInfo newsInfo;
    private Comment comment;
    private UserInfo userInfo;
    private String commentTime;
    private Integer upCount;
    private int isUp;

    public Comment() {
    }

    public Comment(Integer id, String content, NewsInfo newsInfo, Comment comment, UserInfo userInfo, String commentTime, Integer upCount) {
        this.id = id;
        this.content = content;
        this.newsInfo = newsInfo;
        this.comment = comment;
        this.userInfo = userInfo;
        this.commentTime = commentTime;
        this.upCount = upCount;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NewsInfo getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(NewsInfo newsInfo) {
        this.newsInfo = newsInfo;
    }

    public com.example.tt.pojo.UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(com.example.tt.pojo.UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getUpCount() {
        return upCount;
    }

    public void setUpCount(Integer upCount) {
        this.upCount = upCount;
    }

    public Integer getIsUp() {
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }
}
