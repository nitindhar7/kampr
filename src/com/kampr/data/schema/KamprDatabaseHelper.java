package com.kampr.data.schema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KamprDatabaseHelper extends SQLiteOpenHelper {

    // http://www.vogella.de/articles/AndroidSQLite/article.html
    
    protected static final String DATABASE_NAME = "kampr_db";
    protected static final int DATABASE_VERSION = 1;

    public KamprDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        PostSchemaHelper.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        PostSchemaHelper.onUpgrade(database, oldVersion, newVersion);
    }

}
