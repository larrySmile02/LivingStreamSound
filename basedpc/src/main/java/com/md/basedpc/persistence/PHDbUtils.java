package com.md.basedpc.persistence;

import android.content.Context;

import com.md.basedpc.persistence.db.DBTool;
import com.md.basedpc.persistence.db.SQLiteDatabase;

import java.util.List;

/**
 * Created with AndroidStudio3.0.
 * Description:
 * User: ryan
 * Date: 2018-02-11
 * Time: 下午2:03
 */
public class PHDbUtils {
    private static PHDbUtils phDbUtils;

    public PHDbUtils() {
    }

    public static PHDbUtils getInstance() {
        if (phDbUtils == null) {
            synchronized (PHDbUtils.class) {
                if (phDbUtils == null) {
                    phDbUtils = new PHDbUtils();
                }
            }
        }
        return phDbUtils;
    }

    public void initDB(Context context, SQLiteDatabase.DBUpdateListener updateListener) {
        DBTool.getInstance().initDB(context, 1,"phoenix.db", updateListener);
        if (!DBTool.getInstance().getDb().needUpgrade(1)) {
            if (updateListener != null) {
                updateListener.upgraded();
            }
        }
    }

    public SQLiteDatabase getDb() {
        return DBTool.getInstance().getDb();
    }
    /*
     *  各种查询
     */

    public <T> List<T> query(Class<?> clazz, String where) {
        return DBTool.getInstance().getDb().query(clazz, true, where, null, null, null, null);
    }

    public <T> T queryFrist(Class<?> clazz, String where) {
        List<T> l = DBTool.getInstance().getDb().query(clazz, true, where, null, null, null, null);
        return (l == null || l.size() == 0) ? null : l.get(0);
    }

    public <T> List<T> query(Class<?> clazz) {
        return DBTool.getInstance().getDb().query(clazz, true, " 1=1", null, null, null, null);
    }

    public void execute(String sql) {
        DBTool.getInstance().getDb().execute(sql, null);
    }

    public Boolean insert(Object entity) {
        return DBTool.getInstance().getDb().insert(entity);
    }

    public Boolean delete(Class<?> clazz, String where) {
        return DBTool.getInstance().getDb().delete(clazz, where);
    }

    public Boolean delete(Object entity) {
        return DBTool.getInstance().getDb().delete(entity);
    }

    public Boolean update(Object entity) {
        return DBTool.getInstance().getDb().update(entity);
    }

    public Boolean update(Object entity, String where) {
        return DBTool.getInstance().getDb().update(entity, where);
    }
}
