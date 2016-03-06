package com.fly.jiejing.dao;

/**
 * Created by Administrator on 2015/9/23.
 */
public class CreateTable {

    //清洁工
    public static final class createCleanerRankTable {

        public static final String TABLE_NAME = "cleanerTable";
        public static final String PIC = "pic";
        public static final String NAME = "name";
        public static final String CLEANERID = "cleanerid";
        public static final String COUNT = "count";
        public static final String SAVE = "save";


        public static String getCreateSQL() {
            StringBuilder sb = new StringBuilder();
            sb.append("create table if not exists ");
            sb.append(TABLE_NAME + "(");
            sb.append(NAME + " Varchar(15),");
            sb.append(CLEANERID + " Integer(15),");
            sb.append(PIC + " Varchar(225),");
            sb.append(SAVE + " Integer(15),");
            sb.append(COUNT + " Integer(15)");
            sb.append(")");
            return sb.toString();
        }
    }

    //服务项目
    public static final class createItemRankTable {

        public static final String TABLE_NAME = "itemRankTable";
        public static final String PID = "pid";
        public static final String PIC = "pic";
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String NUM = "num";
        public static final String SAVE = "save";

        public static String getCreateSQL() {
            StringBuilder sb = new StringBuilder();
            sb.append("create table if not exists ");
            sb.append(TABLE_NAME + "(");
            sb.append(NAME + " Varchar(15),");
            sb.append(PRICE + " Varchar(255),");
            sb.append(PIC + " Varchar(225),");
            sb.append(NUM + " Integer(30),");
            sb.append(PID + " Integer(15),");
            sb.append(SAVE + " Integer(15)");
            sb.append(")");
            return sb.toString();
        }
    }

    //服务项目
    public static final class createItemTable {

        public static final String TABLE_NAME = "itemTable";
        public static final String UID = "uid";
        public static final String PID = "pid";
        public static final String PIC_CHECK = "pic_check";
        public static final String PIC = "pic";
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String CITYNAME = "cityName";
//        public static final String INTRODUCEURL = "introduceUrl";
        public static final String URL = "url";
        public static final String TYPE = "type";

        public static String getCreateSQL() {
            StringBuilder sb = new StringBuilder();
            sb.append("create table if not exists ");
            sb.append(TABLE_NAME + "(");
            sb.append(UID + " Integer(15),");
            sb.append(PID + " Integer(15),");
            sb.append(NAME + " Varchar(15),");
            sb.append(PRICE + " Varchar(30),");
            sb.append(CITYNAME + " Varchar(15),");
            sb.append(PIC_CHECK + " Varchar(225),");
            sb.append(PIC + " Varchar(225),");
//            sb.append(SUGGEST_MINUTES + " Varchar(225),");
//            sb.append(NAMETITLEURL + " Varchar(225),");
//            sb.append(PERIODREMARKURL + " Varchar(225),");
//            sb.append(PERSONALITY + " Varchar(225),");
            sb.append(URL + " Varchar(225),");
            sb.append(TYPE + " Varchar(15)");
//            sb.append(INTRODUCEURL + " Varchar(225)");
            sb.append(")");
            return sb.toString();
        }
    }


}
