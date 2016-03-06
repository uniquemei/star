package com.fly.jiejing.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/10/2.
 */
public class Items implements Serializable {
    private int id;
    private String name;//例：家庭保洁
    private int uid;
    private String price;
    private int pid;
    private String pic;//项目的图片
    private String pic_check;//项目被选中的状态
    private String suggest_minutes;//建议清理时间
    private String nameTitle;//例：单次baojie_money元/小时(2小时起)
    private String cityName;
    private int cityNum;
    private String nameTitleUrl;//nameTitle可以链接到服务说明
    private String introduceUrl;//http://wap.1jiajie.com/serverinfo/freecode.html?
    private String periodRemarkUrl;//"http://wap.1jiajie.com/serverinfo/cycle_Info.html?"
    private String personality;//个性需求
    private int num;
    private List<Items> child;
    private String url;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic_check() {
        return pic_check;
    }

    public void setPic_check(String pic_check) {
        this.pic_check = pic_check;
    }

    public String getSuggest_minutes() {
        return suggest_minutes;
    }

    public void setSuggest_minutes(String suggest_minutes) {
        this.suggest_minutes = suggest_minutes;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public String getNameTitleUrl() {
        return nameTitleUrl;
    }

    public void setNameTitleUrl(String nameTitleUrl) {
        this.nameTitleUrl = nameTitleUrl;
    }

    public String getIntroduceUrl() {
        return introduceUrl;
    }

    public void setIntroduceUrl(String introduceUrl) {
        this.introduceUrl = introduceUrl;
    }

    public String getPeriodRemarkUrl() {
        return periodRemarkUrl;
    }

    public void setPeriodRemarkUrl(String periodRemarkUrl) {
        this.periodRemarkUrl = periodRemarkUrl;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public List<Items> getChild() {
        return child;
    }

    public void setChild(List<Items> child) {
        this.child = child;
    }
}
