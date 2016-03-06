package com.fly.jiejing.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/9/23.
 */
public class CreateSQLite extends SQLiteOpenHelper{

    public static final String SQLNAME="fly_db";
    public static int VERSION=10;
    public CreateSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public CreateSQLite(Context context){
        this(context,SQLNAME,null,VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable.createItemTable.getCreateSQL());
        db.execSQL(CreateTable.createItemRankTable.getCreateSQL());
        db.execSQL(CreateTable.createCleanerRankTable.getCreateSQL());


//        db.execSQL(CreateTable.blackSMSTable.getCreateSQL());
//        db.execSQL(CreateTable.blackPhoneContactTable.getCreateSQL());
//        db.execSQL(CreateTable.flowDataTable.getCreateSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);

    }
}
