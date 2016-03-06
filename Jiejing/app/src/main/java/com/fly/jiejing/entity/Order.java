package com.fly.jiejing.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/3.
 */
@DatabaseTable(tableName = "order")
public class Order implements Serializable {
    @DatabaseField
    private int id;//订单编号
    @DatabaseField
    private String name;//服务的名字
    private String pic;//服务的图片
    @DatabaseField
    private String user_name;//用户的id
    @DatabaseField
    private int state;//订单的状态
    @DatabaseField
    private String time;//下载订单时的时间
    @DatabaseField
    private String address;//服务地址
    @DatabaseField
    private int item_id;//通过它来查找图片、名称
    @DatabaseField
    private String remark;//个人需求
    @DatabaseField
    private int cleaner_id;
    @DatabaseField
    private String price;
    @DatabaseField
    private int num;
    @DatabaseField
    private String city;
    @DatabaseField
    private int pid;
    @DatabaseField
    private String pic_check;//服务的图片
    private String itemtype;//服务限定
    private String type;//服务限定内容
    private String score;
    private String comment;
    private String userImg;//用户的图片
    private String createtime;//下载订单时的时间
    private String endtime;//下载订单时的时间
    private int price_num;


    public int getPrice_num() {
        return price_num;
    }

    public void setPrice_num(int price_num) {
        this.price_num = price_num;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getCleaner_id() {
        return cleaner_id;
    }

    public void setCleaner_id(int cleaner_id) {
        this.cleaner_id = cleaner_id;
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


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic_check() {
        return pic_check;
    }

    public void setPic_check(String pic_check) {
        this.pic_check = pic_check;
    }

    public Order(int id, String name, String pic, String user_name, int state, String time, String address, String remark,
                 int item_id, int cleaner_id, String price, int num, String city) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.user_name = user_name;
        this.state = state;
        this.time = time;
        this.address = address;
        this.remark = remark;
        this.item_id = item_id;
        this.cleaner_id = cleaner_id;
        this.price = price;
        this.num = num;
        this.city = city;
    }

    public Order() {
        super();
    }
}
