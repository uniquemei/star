package com.fly.jiejing.entity;

/**
 * Created by Administrator on 2015/10/21.
 */
public class LifeType {
    private int id;
    private String type;//类型

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

    public LifeType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public LifeType() {
        super();
    }
}
