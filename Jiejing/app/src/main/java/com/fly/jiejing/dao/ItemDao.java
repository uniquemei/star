package com.fly.jiejing.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fly.jiejing.entity.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/9.服务的表
 */
public class ItemDao {

    private CreateSQLite sqLite;
    public ItemDao(Context context) {
        this.sqLite = new CreateSQLite(context);
    }

    public void addData(Items data) {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = sqLite.getWritableDatabase();
        sb.append(" insert into " + CreateTable.createItemTable.TABLE_NAME + " (");
        sb.append(CreateTable.createItemTable.CITYNAME + ",");
        sb.append(CreateTable.createItemTable.UID + ",");
        sb.append(CreateTable.createItemTable.PID + ",");
        sb.append(CreateTable.createItemTable.NAME + ",");
        //      sb.append(CreateTable.createItemTable.PIC_CHECK + ",");
        sb.append(CreateTable.createItemTable.PIC + ",");
        sb.append(CreateTable.createItemTable.URL + ",");
        sb.append(CreateTable.createItemTable.TYPE+ ",");
        sb.append(CreateTable.createItemTable.PRICE);
        sb.append(" )values(?,?,?,?,?  ,?,?,?) ");

        try {
            db.execSQL(sb.toString(), new Object[]{
                    data.getCityName(), data.getUid(),
                    data.getPid(), data.getName(),
                    data.getPic(), data.getUrl(),
                    data.getType(), data.getPrice()
            });
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }

    public void addAllData(List<Items> datas) {
        if (datas != null && datas.size() > 0) {
            deleCity(datas.get(0).getCityName());
            for (Items data : datas) {
                addData(data);
            }
        }
    }

    public boolean checkCity(String cityName) {
        SQLiteDatabase db = sqLite.getReadableDatabase();
        Cursor cursor = db.query(CreateTable.createItemTable.TABLE_NAME,
                new String[]{CreateTable.createItemTable.NAME}, CreateTable.createItemTable.CITYNAME + " =? ", new String[]{cityName}, null,
                null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public boolean checkName(String data, String cityName) {
        SQLiteDatabase  db = sqLite.getReadableDatabase();
        Cursor cursor = db.query(CreateTable.createItemTable.TABLE_NAME,
                new String[]{CreateTable.createItemTable.NAME}, CreateTable.createItemTable.NAME
                        + "= ? AND " + CreateTable.createItemTable.CITYNAME + " =? ", new String[]{data, cityName}, null,
                null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void deleData(String data, String cityName) {
        SQLiteDatabase  db = this.sqLite.getWritableDatabase();
        db.delete(CreateTable.createItemTable.TABLE_NAME, CreateTable.createItemTable.NAME
                        + "= ? AND " + CreateTable.createItemTable.CITYNAME + " =? ",
                new String[]{data, cityName});
        db.close();
    }

    public void deleCity(String cityName) {
        SQLiteDatabase  db = this.sqLite.getWritableDatabase();
        db.delete(CreateTable.createItemTable.TABLE_NAME, CreateTable.createItemTable.CITYNAME + " =? ",
                new String[]{cityName});
        db.close();
    }

    public List<Items> findAll() {
        List<Items> datas = new ArrayList<>();
        SQLiteDatabase  db = sqLite.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(CreateTable.createItemTable.UID + ",");
        sb.append(CreateTable.createItemTable.PID + ",");
        sb.append(CreateTable.createItemTable.NAME + ",");
        //      sb.append(CreateTable.createItemTable.PIC_CHECK + ",");
        sb.append(CreateTable.createItemTable.PIC + ",");
        sb.append(CreateTable.createItemTable.PRICE + ",");
        sb.append(CreateTable.createItemTable.URL + ",");
        sb.append(CreateTable.createItemTable.TYPE+ ",");

        sb.append(CreateTable.createItemTable.CITYNAME);
        sb.append(" from " + CreateTable.createItemTable.TABLE_NAME);
        //     sb.append(" order by "+CreateTable.createItemTable.UID+" desc ");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        Items data = null;
        while (cursor.moveToNext()) {
            data = new Items();
            data.setUid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.UID)));
            data.setPid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.PID)));
            data.setName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.NAME)));
            data.setPic(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PIC)));
            data.setPrice(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PRICE)));
            data.setUrl(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.URL)));
            data.setType(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.TYPE)));

            data.setCityName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.CITYNAME)));

            datas.add(data);
        }
        cursor.close();
        db.close();
        return datas;

    }

    public List<Items> findAllByCity(String city) {
        List<Items> datas = new ArrayList<>();
        SQLiteDatabase db = sqLite.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(CreateTable.createItemTable.UID + ",");
        sb.append(CreateTable.createItemTable.PID + ",");
        sb.append(CreateTable.createItemTable.NAME + ",");
//        sb.append(CreateTable.createItemTable.PIC_CHECK + ",");
        sb.append(CreateTable.createItemTable.PIC + ",");
        sb.append(CreateTable.createItemTable.PRICE + ",");
        sb.append(CreateTable.createItemTable.URL + ",");
        sb.append(CreateTable.createItemTable.TYPE + ",");

        sb.append(CreateTable.createItemTable.CITYNAME);
        sb.append(" from " + CreateTable.createItemTable.TABLE_NAME);
        sb.append(" where " + CreateTable.createItemTable.CITYNAME + " =?");
        //     sb.append(" order by "+CreateTable.createItemTable.UID+" desc ");
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{city});
        Items data = null;
        while (cursor.moveToNext()) {
            data = new Items();
            data.setUid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.UID)));
            data.setPid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.PID)));
            data.setName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.NAME)));
            data.setPic(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PIC)));
            data.setPrice(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PRICE)));
            data.setUrl(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.URL)));
            data.setType(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.TYPE)));
            data.setCityName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.CITYNAME)));

            datas.add(data);
        }
        cursor.close();
        db.close();
        return datas;

    }

    public List<Items> findTop() {
        List<Items> datas = new ArrayList<>();
        SQLiteDatabase db = sqLite.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(CreateTable.createItemTable.UID + ",");
        sb.append(CreateTable.createItemTable.PID + ",");
        sb.append(CreateTable.createItemTable.NAME + ",");
        //      sb.append(CreateTable.createItemTable.PIC_CHECK + ",");
        sb.append(CreateTable.createItemTable.PIC + ",");
        sb.append(CreateTable.createItemTable.PRICE + ",");
        sb.append(CreateTable.createItemTable.URL + ",");
        sb.append(CreateTable.createItemTable.TYPE+ ",");
        sb.append(CreateTable.createItemTable.CITYNAME);
        sb.append(" from " + CreateTable.createItemTable.TABLE_NAME);
        sb.append(" where " + CreateTable.createItemTable.CITYNAME + " =?");
        //     sb.append(" order by "+CreateTable.createItemTable.UID+" desc ");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        Items data = null;
        int i = 0;
        while (cursor.moveToNext()) {
            data = new Items();
            data.setUid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.UID)));
            data.setPid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.PID)));
            data.setName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.NAME)));
            data.setPic(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PIC)));
            data.setPrice(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PRICE)));
            data.setUrl(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.URL)));
            data.setType(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.TYPE)));

            data.setCityName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.CITYNAME)));

            datas.add(data);
            i++;
            if (i == 3) {
                cursor.close();
                db.close();
                return datas;
            }
        }
        cursor.close();
        db.close();
        return datas;

    }

    public Items findAllByItem(String uid) {
        SQLiteDatabase db = sqLite.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(CreateTable.createItemTable.UID + ",");
        sb.append(CreateTable.createItemTable.PID + ",");
        sb.append(CreateTable.createItemTable.NAME + ",");
//        sb.append(CreateTable.createItemTable.PIC_CHECK + ",");
        sb.append(CreateTable.createItemTable.PIC + ",");
        sb.append(CreateTable.createItemTable.PRICE + ",");
        sb.append(CreateTable.createItemTable.URL + ",");
        sb.append(CreateTable.createItemTable.TYPE+ ",");
        sb.append(CreateTable.createItemTable.CITYNAME);
        sb.append(" from " + CreateTable.createItemTable.TABLE_NAME);
        sb.append(" where " + CreateTable.createItemTable.UID + " =?");
        Cursor cursor = db.rawQuery(sb.toString(), new String[]{uid});
        Items data = new Items();
        while (cursor.moveToNext()) {
            data.setUid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.UID)));
            data.setPid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemTable.PID)));
            data.setName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.NAME)));
            data.setPic(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PIC)));
            data.setPrice(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.PRICE)));
            data.setUrl(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.URL)));
            data.setType(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.TYPE)));
            data.setCityName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemTable.CITYNAME)));

        }
        cursor.close();
        db.close();
        return data;

    }

    public void deleAll() {
        SQLiteDatabase db = this.sqLite.getWritableDatabase();
        db.delete(CreateTable.createItemTable.TABLE_NAME, null,
                null);
        db.close();


    }
}
