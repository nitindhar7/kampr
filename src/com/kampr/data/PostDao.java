package com.kampr.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.kampr.data.schema.KamprDatabaseHelper;
import com.kampr.data.schema.PostSchemaHelper;
import com.kampr.models.Post;
import com.kampr.util.TextUtils;

public class PostDao {
    
    private static final String GET_POSTS_FROM_LAST_WEEK =
        "SELECT " +
            "post_id, " +
            "created_at, " +
            "username, " +
            "title, " +
            "url, " +
            "type, " +
            "content, " +
            "description, " +
            "snap, " +
            "view_count, " +
            "like_count, " +
            "comment_count " +
         "FROM posts " +
         "WHERE DATETIME(created_at) > DATETIME(JULIANDAY(DATE('now')) - 8)";

    private static Context _context;
    private static SQLiteDatabase _db;
    private static KamprDatabaseHelper _dbHelper;

    public PostDao(Context context) {
        _context = context;
    }

    public void open() throws SQLException {
        _dbHelper = new KamprDatabaseHelper(_context);
        _db = _dbHelper.getWritableDatabase();
    }

    public void close() {
        _dbHelper.close();
    }
    
    public List<Post> getPostsFromLastWeek() {
        List<Post> posts = null;
        Cursor cursor = _db.rawQuery(GET_POSTS_FROM_LAST_WEEK, null);
        
        try {
            posts = new ArrayList<Post>();
            
            if (cursor != null) {
                while(cursor.moveToNext()) {
                    Log.i("POSTDAO", "username: " + cursor.getString(2));
                    Post post = new Post();
                    post.setId(cursor.getInt(0));
                    post.setCreatedAt(cursor.getString(1));
                    post.setUserName(cursor.getString(2));
                    post.setTitle(cursor.getString(3));
                    post.setUrl(cursor.getString(4));
                    post.setType(cursor.getString(5));
                    post.setContent(cursor.getString(6));
                    post.setDescription(cursor.getString(7));
                    post.setSnap(cursor.getString(8));
//                    String filename = TextUtils.toMD5(post.getUser().getUsername());
//                    post.setUserIcon(BitmapFactory.decodeFile(filename));
                    post.setViewCount(cursor.getInt(9));
                    post.setLikeCount(cursor.getInt(10));
                    post.setCommentCount(cursor.getInt(11));                
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            posts = null;
            Log.e("KAMPR-POSTS-DATA-GET", "Error getting posts from last week");
        } finally {
            cursor.close();
        }
        
        return posts;
    }
    
    public int setPostsForPastWeek(List<Post> posts) {
        int postsCached = 0;

        try {
            for(Post post : posts) {
                ContentValues values = new ContentValues();
                values.put("post_id", post.getId());
                values.put("created_at", post.getCreatedAt());
                Log.i("POSTDAO", post.getCreatedAt());
                values.put("username", post.getUserName());
                values.put("title", post.getTitle());
                values.put("url", post.getUrl());
                values.put("type", post.getType());
                values.put("content", post.getContent());
                values.put("description", post.getDescription());
                values.put("snap", post.getSnap());
                String filename = TextUtils.toMD5(post.getUser().getUsername());
                post.getUserIcon().compress(CompressFormat.PNG, 100, _context.openFileOutput(filename, Context.MODE_PRIVATE));
                values.put("user_icon_filename", filename);
                values.put("view_count", post.getViewCount());
                values.put("like_count", post.getLikeCount());
                values.put("comment_count", post.getCommentCount());
                _db.insert(PostSchemaHelper.TABLE_NAME, null, values);
            }

            postsCached = posts.size();
        } catch (Exception e) {
            Log.e("KAMPR-POSTS-DATA-SET", "Error setting posts for past week");
        }
        
        return postsCached;
    }

}
