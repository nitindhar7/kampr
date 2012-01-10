package com.kampr.data.schema;

import android.database.sqlite.SQLiteDatabase;

public class PostSchemaHelper {
    
    public static final String TABLE_NAME = "posts";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_POST_ID = "post_id";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SNAP = "snap";
    public static final String KEY_USER_ICON_FILENAME = "user_icon_filename";
    public static final String KEY_VIEW_COUNT = "view_count";
    public static final String KEY_LIKE_COUNT = "like_count";
    public static final String KEY_COMMENT_COUNT = "comment_count";

    private static final String DATABASE_CREATE =
        "CREATE TABLE " + TABLE_NAME + " ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "post_id INTEGER NOT NULL, " +
            "created_at TEXT NOT NULL, " +
            "username TEXT NOT NULL, " +
            "title TEXT NOT NULL, " +
            "url TEXT, " +
            "type TEXT NOT NULL, " +
            "content TEXT, " +
            "description TEXT, " +
            "snap TEXT, " +
            "user_icon_filename TEXT NOT NULL, " +
            "view_count INTEGER NOT NULL, " +
            "like_count INTEGER NOT NULL, " +
            "comment_count INTEGER NOT NULL, " +
            "UNIQUE(post_id) " +
        ");";
    
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS posts");
        onCreate(database);
    }

}
