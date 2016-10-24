package com.wyu.earnmoney.network.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rays on 16/7/8.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATA_NAME = "cache.db";
    private static int version = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATA_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS url_cache (id integer PRIMARY KEY AUTOINCREMENT, status_code integer NOT NULL, cache_key text UNIQUE NOT NULL, data text NOT NULL, updatetime timestamp NOT NULL DEFAULT(datetime('now','localtime')), validity integer NOT NULL DEFAULT(-1))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
