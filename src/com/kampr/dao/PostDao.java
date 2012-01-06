package com.kampr.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PostDao {

    private static final String DATABASE_CREATE =
        "CREATE TABLE posts ( " +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "post_id INTEGER NOT NULL, " +
            "created_at TEXT NOT NULL, " +
            "username TEXT NOT NULL, " +
            "title TEXT NOT NULL, " +
            "url TEXT NOT NULL, " +
            "type TEXT NOT NULL, " +
            "content TEXT NOT NULL, " +
            "description TEXT NOT NULL, " +
            "snap TEXT NOT NULL, " +
            "user_icon_filename TEXT NOT NULL, " +
            "view_count INTEGER NOT NULL, " +
            "like_count INTEGER NOT NULL, " +
            "comment_count INTEGER NOT NULL " +
        ");";
    
    private static Context _context;
    private static SQLiteDatabase _db;
    private static KamprDatabaseHelper _dbHelper;

    public PostDao(Context context) {
        _context = context;
    }
    
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS posts");
        onCreate(database);
    }

    public static void open() throws SQLException {
        _dbHelper = new KamprDatabaseHelper(_context);
        _db = _dbHelper.getWritableDatabase();
    }

    public static void close() {
        _dbHelper.close();
    }

}
