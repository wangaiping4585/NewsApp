package com.example.tt.pojo;

import java.io.Serializable;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsInfo implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private NewsType newsType;
    private String pubTime;
    private String pubIns;
    private Integer commentCount;
    private NewsPic newsPic;
    private NewsPic newsPic2;
    private NewsPic newsPic3;
    private Integer type;
    private Integer hasCollected;

    public NewsInfo() {
    }

    public NewsInfo(Integer id, String title, String content, NewsType newsType, String pubTime, String pubIns, Integer commentCount, NewsPic newsPic, NewsPic newsPic2, NewsPic newsPic3, Integer type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.newsType = newsType;
        this.pubTime = pubTime;
        this.pubIns = pubIns;
        this.commentCount = commentCount;
        this.newsPic = newsPic;
        this.newsPic2 = newsPic2;
        this.newsPic3 = newsPic3;
        this.type = type;
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

    public NewsType getNewsType() {
        return newsType;
    }

    public void setNewsType(NewsType newsType) {
        this.newsType = newsType;
    }

    public String getPubIns() {
        return pubIns;
    }

    public void setPubIns(String pubIns) {
        this.pubIns = pubIns;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public NewsPic getNewsPic() {
        return newsPic;
    }

    public void setNewsPic(NewsPic newsPic) {
        this.newsPic = newsPic;
    }

    public NewsPic getNewsPic2() {
        return newsPic2;
    }

    public void setNewsPic2(NewsPic newsPic2) {
        this.newsPic2 = newsPic2;
    }

    public NewsPic getNewsPic3() {
        return newsPic3;
    }

    public void setNewsPic3(NewsPic newsPic3) {
        this.newsPic3 = newsPic3;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHasCollected() {
        return hasCollected;
    }

    public void setHasCollected(Integer hasCollected) {
        this.hasCollected = hasCollected;
    }
}
