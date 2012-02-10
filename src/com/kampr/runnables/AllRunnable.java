package com.kampr.runnables;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.forrst.api.model.Post;
import com.forrst.api.model.Snap;
import com.forrst.api.model.User;
import com.kampr.R;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.PostDecorator;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;
import com.kampr.util.TimeUtils;

public class AllRunnable extends PostsRunnable {

    public AllRunnable(Context context, PostsHandler<PostDecorator> handler, List<PostDecorator> posts, Map<String,String> forrstParams) {
        super(context, handler, posts, forrstParams, null);
    }
    
    @Override
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsAll(null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                JSONObject json = postsJSONArray.getJSONObject(count);
                JSONObject userJson = json.getJSONObject("user");
                
                Bitmap userIcon = ImageUtils.fetchUserIcon(_context, userJson.getJSONObject("photos").getString("medium_url"), R.drawable.forrst_default_25);
                
                User user = new User();
                user.setId(userJson.getInt("id"));
                user.setUsername(userJson.getString("username"));
                user.setName(userJson.getString("name"));
                user.setUrl(userJson.getString("url"));
                user.setPosts(userJson.getInt("posts"));
                user.setComments(userJson.getInt("comments"));
                user.setLikes(userJson.getInt("likes"));
                user.setFollowers(userJson.getInt("followers"));
                user.setFollowing(userJson.getInt("following"));
                user.setBio(TextUtils.convertHtmlToText(userJson.getString("bio")));
                user.setIsA(userJson.getString("is_a"));
                user.setHomepageUrl(userJson.getString("homepage_url"));
                if (userJson.has("twitter")) {
                    user.setTwitter(userJson.getString("twitter"));
                }
                user.setTagString(userJson.getString("tag_string"));

                Post post = new Post();
                post.setId(json.getInt("id"));
                post.setUser(user);
                post.setPostType(json.getString("post_type"));
                post.setCreatedAt(TimeUtils.getPostDate(json.getString("created_at")));
                post.setTitle(json.getString("title"));
                post.setUrl(json.getString("url"));
                post.setContent(json.getString("content"));
                post.setDescription(json.getString("description"));
                post.setViewCount(json.getInt("view_count"));
                post.setLikeCount(json.getInt("like_count"));
                post.setCommentCount(json.getInt("comment_count"));
                if (json.getString("post_type").equals("snap")) {
                    Snap snap = new Snap();
                    snap.setOriginalUrl(json.getJSONObject("snaps").getString("original_url"));
                    post.setSnap(snap);
                }
                
                PostDecorator pd = new PostDecorator();
                pd.setPost(post);
                pd.setUserIcon(userIcon);

                _posts.add(pd);
            }

            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching posts from Forrst", e);
        }
    }

}