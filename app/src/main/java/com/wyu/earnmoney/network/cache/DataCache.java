package com.wyu.earnmoney.network.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wyu.earnmoney.utils.LogUtils;


/**
 * Created by Rays on 16/7/8.
 */
public class DataCache {
    private static final String TABLE_NAME = "url_cache";
    private static final String FIELD_CACHE_KEY = "cache_key";
    private static final String FIELD_STATUS_CODE = "status_code";
    private static final String FIELD_DATA = "data";
    private static final String FIELD_VALIDITY = "validity";
    private static final String FIELD_UPDATETIME = "updatetime";
    private static final int AWAY_VALIDITY = -1;
    private static final DataCache INSTANCE = new DataCache();
    private MyDatabaseHelper myDatabaseHelper;

    public static DataCache getInstance() {
        return INSTANCE;
    }

    private DataCache() {

    }

    public void init(Context context) {
        myDatabaseHelper = new MyDatabaseHelper(context);
    }

    public synchronized long save(String cacheKey, int statusCode, String data, long validity) {
        LogUtils.i("============save cache===========");
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_CACHE_KEY, cacheKey);
        contentValues.put(FIELD_DATA, data);
        contentValues.put(FIELD_STATUS_CODE, statusCode);
        contentValues.put(FIELD_VALIDITY, validity);
        contentValues.put(FIELD_UPDATETIME, System.currentTimeMillis());
        return db.insertWithOnConflict(TABLE_NAME, FIELD_DATA, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public synchronized CacheEntity query(String cacheKey) {
        LogUtils.i("============query cache===========");
        CacheEntity entity = null;
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + FIELD_STATUS_CODE + "," + FIELD_DATA + "," + FIELD_VALIDITY + "," + FIELD_UPDATETIME + ",(" + System.currentTimeMillis()
                        + "-" + FIELD_UPDATETIME + ") AS cha  FROM " + TABLE_NAME + " WHERE cache_key == ?",
                new String[]{cacheKey});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                entity = new CacheEntity();
                entity.statusCode = cursor.getInt(cursor.getColumnIndex(FIELD_STATUS_CODE));
                entity.updateTime = cursor.getLong(cursor.getColumnIndex(FIELD_UPDATETIME));
                entity.data = cursor.getString(cursor.getColumnIndex(FIELD_DATA));
                entity.validity = cursor.getLong(cursor.getColumnIndex(FIELD_VALIDITY));
                long cha = cursor.getLong(cursor.getColumnIndex("cha"));
                // 是否有效
                if (entity.validity == AWAY_VALIDITY || cha <= entity.validity) {
                    entity.isAvail = true;
                } else {
                    entity.isAvail = false;
                }
                LogUtils.i("============query cache success isAvail=" + entity.isAvail);
            }
            cursor.close();
        }
        return entity;
    }

    public void closeDatabase() {
        if (myDatabaseHelper != null) {
            myDatabaseHelper.close();
        }
    }

}
