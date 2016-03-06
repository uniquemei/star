package com.fly.jiejing.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lewei on 2015/10/20.
 */
public class CleanerDao {
    private CreateSQLite sqLite;

    public CleanerDao(Context context) {
        this.sqLite = new CreateSQLite(context);
    }

    public void addData(Cleaner data) {
        StringBuilder sb = new StringBuilder();
        SQLiteDatabase db = sqLite.getWritableDatabase();
        sb.append(" insert into " + CreateTable.createCleanerRankTable.TABLE_NAME + " (");
        sb.append(CreateTable.createCleanerRankTable.SAVE + ",");
        sb.append(CreateTable.createCleanerRankTable.NAME + ",");
        sb.append(CreateTable.createCleanerRankTable.PIC + ",");
        sb.append(CreateTable.createCleanerRankTable.CLEANERID + ",");
        sb.append(CreateTable.createCleanerRankTable.COUNT);
        sb.append(" )values(?,?,?,?,? ) ");
        try {
            db.execSQL(sb.toString(), new Object[]{data.getSave(), data.getName(),
                    data.getPic(), data.getCleanerid(), data.getCount()
            });
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }
//添加清洁工
    public void addAllData(List<Cleaner> datas) {
        if (datas != null && datas.size() > 0) {
            deleAll();
            for (Cleaner data : datas) {
                addData(data);
            }
        }
    }

    //找到所有清洁工
    public List<Cleaner> findAll() {
        List<Cleaner> datas = new ArrayList<>();
        SQLiteDatabase db = sqLite.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(CreateTable.createCleanerRankTable.SAVE + ",");
        sb.append(CreateTable.createCleanerRankTable.NAME + ",");
        sb.append(CreateTable.createCleanerRankTable.PIC + ",");
        sb.append(CreateTable.createCleanerRankTable.CLEANERID + ",");
        sb.append(CreateTable.createCleanerRankTable.COUNT);
        sb.append(" from " + CreateTable.createCleanerRankTable.TABLE_NAME);
        sb.append(" order by " + CreateTable.createCleanerRankTable.COUNT + " desc");
        Cursor cursor = db.rawQuery(sb.toString(), null);
        Cleaner data;
        while (cursor.moveToNext()) {
            data = new Cleaner();
            data.setSave(cursor.getInt(cursor.getColumnIndex(CreateTable.createCleanerRankTable.SAVE)));
            data.setName(cursor.getString(cursor.getColumnIndex(CreateTable.createCleanerRankTable.NAME)));
            data.setPic(cursor.getString(cursor.getColumnIndex(CreateTable.createCleanerRankTable.PIC)));
            data.setCleanerid(cursor.getInt(cursor.getColumnIndex(CreateTable.createCleanerRankTable.CLEANERID)));
            data.setCount(cursor.getInt(cursor.getColumnIndex(CreateTable.createCleanerRankTable.COUNT)));
            datas.add(data);
        }
        cursor.close();
        db.close();
        return datas;

    }

    //删除所有项目
    public void deleAll() {
        SQLiteDatabase db = this.sqLite.getWritableDatabase();
        db.delete(CreateTable.createCleanerRankTable.TABLE_NAME, null,
                null);
        db.close();
    }

    //通过proName更新
    public void updateSave(Cleaner cleaner){
        SQLiteDatabase db = sqLite.getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append(" update ");
        sb.append(CreateTable.createCleanerRankTable.TABLE_NAME +" set ");
        sb.append(CreateTable.createCleanerRankTable.SAVE + "=? where ");
        sb.append(CreateTable.createCleanerRankTable.CLEANERID + "=?");
        db.execSQL(sb.toString(), new Object[]{cleaner.getSave(),cleaner.getCleanerid()});
        db.close();
    }
}
