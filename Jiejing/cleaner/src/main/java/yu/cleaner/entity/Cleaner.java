package yu.cleaner.entity;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/5.
 */

public class Cleaner implements Serializable {

    private String userid;

    private int cleanerid;

    private String name;

    private String pic;

    private String grade;//评分

    private int count;//服务次数

    private int pid;

    private int type;
    private int isblack;//是否是黑名单
    private int iscommon;//是否是常用阿姨
    private int selected;
    private int save;
    private String city;
    private String phone;

    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
