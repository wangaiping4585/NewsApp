package com.example.tt.pojo;

import java.io.Serializable;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsType implements Serializable {
    private Integer id;
    private String name;

    public NewsType() {
    }

    public NewsType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
