package com.fly.jiejing.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2015/10/2.
 */
@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField
    private String user_id;
    @DatabaseField
    private String pic;//用户头像
    @DatabaseField
    private String score;//积分
    @DatabaseField
    private String personality;//个性需求
    @DatabaseField
    private String balance;//余额
    @DatabaseField
    private int coupon_num;//优惠券的张数
    @DatabaseField
    private String invitation_id;

    public String getInvitation_id() {
        return invitation_id;
    }

    public void setInvitation_id(String invitation_id) {
        this.invitation_id = invitation_id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public int getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(int coupon_num) {
        this.coupon_num = coupon_num;
    }

    public User(String user_id, String pic, String score, String personality, String balance, int coupon_num) {
        this.user_id = user_id;
        this.pic = pic;
        this.score = score;
        this.personality = personality;
        this.balance = balance;
        this.coupon_num = coupon_num;
    }

    public User() {
        super();
    }
}
