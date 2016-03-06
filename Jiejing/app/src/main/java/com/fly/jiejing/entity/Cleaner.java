package com.fly.jiejing.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/5.
 */
@DatabaseTable(tableName = "cleaner")
public class Cleaner implements Serializable {
    @DatabaseField
    private String userid;
    @DatabaseField(id = true)
    private int cleanerid;
    @DatabaseField
    private String name;
    @DatabaseField
    private String pic;
    @DatabaseField
    private String grade;//评分
    @DatabaseField
    private int count;//服务次数
    @DatabaseField
    private int pid;
    @DatabaseField
    private int type;
    private int isblack;//是否是黑名单
    private int iscommon;//是否是常用阿姨
    private int selected;
    private int save;
    private String city;


    public String  getType() {
        String t="";
        switch (pid){
            case 1:
                t="家庭清洁";
                break;
            case 2:
                t="家电清洗";
                break;
            case 3:
                t="家具养护";
                break;
            case 4:
                t="洗护服务";
                break;
            case 5:
                t="水电服务";
                break;
        }

        return t;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private boolean isSelect = false;//删除是否被选中

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getCleanerid() {
        return cleanerid;
    }

    public void setCleanerid(int cleanerid) {
        this.cleanerid = cleanerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIscommon() {
        return iscommon;
    }

    public void setIscommon(int iscommon) {
        this.iscommon = iscommon;
    }

    public int getIsblack() {
        return isblack;
    }

    public void setIsblack(int isblack) {
        this.isblack = isblack;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Cleaner(String userid, int cleanerid, String name, String pic, String grade, int count, int isblack, int iscommon, boolean isSelect) {
        this.userid = userid;
        this.cleanerid = cleanerid;
        this.name = name;
        this.pic = pic;
        this.grade = grade;
        this.count = count;
        this.isblack = isblack;
        this.iscommon = iscommon;
        this.isSelect = isSelect;
    }

    public Cleaner() {
        super();
    }
}
