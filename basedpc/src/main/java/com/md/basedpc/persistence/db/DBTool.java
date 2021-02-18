package com.md.basedpc.persistence.db;

import android.content.Context;


/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2016/7/6 12:06.
 */

public class DBTool {
    // 数据库默认设置
    public static String DB_NAME = "yxt.db"; // 默认数据库名字
    private int DB_VERSION = 1;// 默认数据库版本
    private Context mContext;
    private static DBTool utilInstance;
    private SQLiteDatabase db;
    private SQLiteDatabasePool mSQLiteDatabasePool;


    public static synchronized DBTool getInstance() {
        if (utilInstance == null) {
            utilInstance = new DBTool();
        }
        return utilInstance;
    }
    public void initDB(Context context, int v, String n, SQLiteDatabase.DBUpdateListener tadbUpdateListener) {
        mContext = context;
        DB_NAME = n;
        DB_VERSION = v;
        setDb(getSQLiteDatabasePool(tadbUpdateListener).getSQLiteDatabase());
    }

    public SQLiteDatabasePool getSQLiteDatabasePool(SQLiteDatabase.DBUpdateListener tadbUpdateListener) {
        if (mSQLiteDatabasePool == null) {
            mSQLiteDatabasePool = SQLiteDatabasePool.getInstance(mContext, DB_NAME, DB_VERSION, true);
            mSQLiteDatabasePool.setOnDbUpdateListener(tadbUpdateListener);
            mSQLiteDatabasePool.createPool();
        }
        return mSQLiteDatabasePool;
    }

    public SQLiteDatabase getDb() {
        if (db == null)
            db = getSQLiteDatabasePool(null).getSQLiteDatabase();
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

}
