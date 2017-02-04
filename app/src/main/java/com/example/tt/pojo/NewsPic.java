package com.example.tt.pojo;

import java.io.Serializable;

/**
 * Created by TT on 2016/9/18.
 */
public class NewsPic implements Serializable {
    private Integer id;
    private Integer src;
    private String desc;

    public NewsPic() {
    }

    public NewsPic(String desc, Integer id, Integer src) {
        this.desc = desc;
        this.id = id;
        this.src = src;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSrc() {
        return src;
    }

    public void setSrc(Integer src) {
        this.src = src;
    }
}
