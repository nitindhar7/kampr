package com.kampr.runnables;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.forrst.api.model.Post;
import com.kampr.R;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.PostDecorator;
import com.kampr.models.User;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;
import com.kampr.util.TimeUtils;

public class PostsRunnable extends AbstractRunnable {

    protected String _postType;
    protected Map<String,String> _forrstParams;
    protected List<PostDecorator> _posts;
    
    public PostsRunnable(Context context, PostsHandler<PostDecorator> handler, List<PostDecorator> posts, Map<String,String> forrstParams, String postType) {
        super(context, handler);
        _forrstParams = forrstParams;
        _postType = postType;
        _posts = posts;
    }
    
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsList(_postType, _forrstParams);
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
                user.setPostsCount(userJson.getInt("posts"));
                user.setCommentsCount(userJson.getInt("comments"));
                user.setLikesCount(userJson.getInt("likes"));
                user.setFollowersCount(userJson.getInt("followers"));
                user.setFollowingCount(userJson.getInt("following"));
                user.setBio(TextUtils.convertHtmlToText(userJson.getString("bio")));
                user.setRole(userJson.getString("is_a"));
                user.setHomepageUrl(userJson.getString("homepage_url"));
                if (userJson.has("twitter")) {
                    user.setTwitter(userJson.getString("twitter"));
                }
                user.setTagString(userJson.getString("tag_string"));
                user.setUserIcon(userIcon);

                Post post = new Post();
                post.setId(json.getInt("id"));
                post.setUser(user);
                post.setType(json.getString("post_type"));
                post.setCreatedAt(TimeUtils.getPostDate(json.getString("created_at")));
                post.setUserName(user.getName());
                post.setTitle(json.getString("title"));
                post.setUrl(json.getString("url"));
                post.setContent(json.getString("content"));
                post.setDescription(json.getString("description"));
                post.setViewCount(json.getInt("view_count"));
                post.setLikeCount(json.getInt("like_count"));
                post.setCommentCount(json.getInt("comment_count"));
                post.setUserIcon(userIcon);
                if (json.has("snaps")) {
                    post.setSnap(json.getJSONObject("snaps").getString("original_url"));
                }

                _posts.add(post);
            }

            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching post from Forrst", e);
        }
    }

}