package com.example.tt.pojo;

import java.io.Serializable;

/**
 * Created by TT on 2016/9/18.
 */
public class UserInfo implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private Integer src;
    private String registerTime;
    private Integer collectionCount;

    public UserInfo() {
    }

    public UserInfo(Integer id, String nickname, String password, String registerTime, Integer src, String username) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.registerTime = registerTime;
        this.src = src;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getSrc() {
        return src;
    }

    public void setSrc(Integer src) {
        this.src = src;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(Integer collectionCount) {
        this.collectionCount = collectionCount;
    }
}
