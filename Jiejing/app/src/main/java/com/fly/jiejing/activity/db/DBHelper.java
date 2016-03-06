package com.fly.jiejing.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fly.jiejing.entity.Address;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.Life;
import com.fly.jiejing.entity.LifeType;
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.entity.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2015/10/8.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASENAME = "blackCleaner";
    private static final int VERSION = 10;
    private static DBHelper hepler;

    public DBHelper(Context context) {
        super(context, DATABASENAME, null, VERSION);
    }

    //单利模式
    public static DBHelper getInstance(Context context) {
        synchronized (DBHelper.class) {
            if (hepler == null) {
                synchronized (DBHelper.class) {
                    if (hepler == null) {
                        hepler = new DBHelper(context);
                    }
                }
            }
        }
        return hepler;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Cleaner.class);
            TableUtils.createTable(connectionSource, Life.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Order.class);
            TableUtils.createTable(connectionSource, Address.class);
            //   TableUtils.createTable(connectionSource, LifeType.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    private Dao<Cleaner, Integer> blackCleanerDao = null;

    public Dao<Cleaner, Integer> getBlackCleanerDao() {
        if (blackCleanerDao == null) {
            try {
                blackCleanerDao = getDao(Cleaner.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return blackCleanerDao;
    }

    private Dao<Life, Integer> lifeDao = null;

    public Dao<Life, Integer> getLifeDao() {
        if (lifeDao == null) {
            try {
                lifeDao = getDao(Life.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lifeDao;
    }

    //用户
    private Dao<User, Integer> userDao = null;

    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                userDao = getDao(User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }

    //订单表
    private Dao<Order, Integer> orderDao = null;

    public Dao<Order, Integer> getorderDao() {
        if (orderDao == null) {
            try {
                orderDao = getDao(Order.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return orderDao;
    }

    //地址表
    private Dao<Address, Integer> addresseDao = null;

    public Dao<Address, Integer> getaddresseDao() {
        if (addresseDao == null) {
            try {
                addresseDao = getDao(Address.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return addresseDao;
    }


//    private Dao<LifeType, Integer> lifeTypeDao = null;
//    public Dao<LifeType, Integer> getLifeTypeDao() {
//        if (lifeTypeDao == null) {
//            try {
//                lifeTypeDao = getDao(LifeType.class);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return lifeTypeDao;
//    }

    @Override
    public void close() {
        super.close();
        blackCleanerDao = null;
        lifeDao = null;
        userDao = null;
        orderDao = null;
        addresseDao=null;
    }
}


