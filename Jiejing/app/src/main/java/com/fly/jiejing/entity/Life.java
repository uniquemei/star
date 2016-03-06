package com.fly.jiejing.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/21.
 */
@DatabaseTable(tableName = "lifeTable")
public class Life implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;//每个item的id
    @DatabaseField
    private int typeid;//类型的id
    @DatabaseField
    private String title;
    @DatabaseField
    private String time;//时间
    @DatabaseField
    private String url;//连接
    @DatabaseField
    private String img;
    @DatabaseField
    private String type;

    public Life() {
        super();
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public Life(int id, String type, String title, String time, String img, String url) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.time = time;
        this.img = img;
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
