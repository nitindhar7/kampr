package com.kampr.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KamprDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kampr_db";
    private static final int DATABASE_VERSION = 1;

    public KamprDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        PostDao.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        PostDao.onUpgrade(database, oldVersion, newVersion);
    }

}
