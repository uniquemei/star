package com.fly.jiejing.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fly.jiejing.entity.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lewei on 2015/10/20.
 */
public class ItemOrderDao {
    private CreateSQLite sqLite;

    public ItemOrderDao(Context context) {
        this.sqLite = new CreateSQLite(context);
    }

    public void addData(Items data) {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = sqLite.getWritableDatabase();
        sb.append(" insert into " + CreateTable.createItemRankTable.TABLE_NAME + " (");
        sb.append(CreateTable.createItemRankTable.NAME + ",");
        sb.append(CreateTable.createItemRankTable.PIC + ",");
        sb.append(CreateTable.createItemRankTable.NUM + ",");
        sb.append(CreateTable.createItemRankTable.PID + ",");
        sb.append(CreateTable.createItemRankTable.PRICE);

        sb.append(" )values(?,?,?,?,? ) ");
        try {
            db.execSQL(sb.toString(), new Object[]{ data.getName(),
                    data.getPic(), data.getNum(),data.getPid(),data.getPrice()
            });
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }
    public void addAllData(List<Items> datas) {
        if(datas!=null&&datas.size()>0){
            deleAll();
            for (Items data : datas) {
                addData(data);
            }
        }
    }
    //找到所有项目
    public List<Items> findAll() {
        List<Items> datas = new ArrayList<>();
        SQLiteDatabase db = sqLite.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(CreateTable.createItemRankTable.NAME + ",");
        sb.append(CreateTable.createItemRankTable.PIC + ",");
        sb.append(CreateTable.createItemRankTable.NUM + ",");
        sb.append(CreateTable.createItemRankTable.PID + ",");

        sb.append(CreateTable.createItemRankTable.PRICE);
        sb.append(" from " + CreateTable.createItemRankTable.TABLE_NAME);
        sb.append(" order by "+CreateTable.createItemRankTable.NUM+" desc");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        Items data;
        while (cursor.moveToNext()) {
            data = new Items();
            data.setName(cursor.getString(cursor.getColumnIndex(CreateTable.createItemRankTable.NAME)));
            data.setPic(cursor.getString(cursor.getColumnIndex(CreateTable.createItemRankTable.PIC)));
            data.setNum(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemRankTable.NUM)));
            data.setPid(cursor.getInt(cursor.getColumnIndex(CreateTable.createItemRankTable.PID)));
            data.setPrice(cursor.getString(cursor.getColumnIndex(CreateTable.createItemRankTable.PRICE)));
            datas.add(data);
        }
        cursor.close();
        db.close();
        return datas;

    }

    //删除所有项目
    public void deleAll() {
        SQLiteDatabase db = this.sqLite.getWritableDatabase();
        db.delete(CreateTable.createItemRankTable.TABLE_NAME, null,
                null);
        db.close();


    }
}
