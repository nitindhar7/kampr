package com.kampr.posts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kampr.PostsActivity;
import com.kampr.R;
import com.kampr.UserActivity;
import com.kampr.adapters.PostsAdapter;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.Post;
import com.kampr.models.User;
import com.kampr.runnables.AllRunnable;
import com.kampr.runnables.PostsRunnable;
import com.kampr.util.ImageUtils;
import com.kampr.util.NetworkUtils;

public class PostsListActivity<T> extends ListActivity implements OnItemClickListener {

    protected ListView _posts;
    protected Map<String,Bitmap> _userIcons;
    protected List<Post> _listOfPosts;
    protected Thread _fetchPostsThread;
    protected PostsHandler<Post> _handler;
    protected PostsAdapter<T> _postsAdapter;
    protected String _postsType;
    protected boolean _trueResume;
    
    public PostsListActivity() {
        NetworkUtils.trustAllHosts();
        _userIcons = new HashMap<String,Bitmap>();
        _listOfPosts = new ArrayList<Post>();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PostsActivity.getSpinner().setVisibility(View.VISIBLE);
        
        _posts = getListView();
        _posts.setVerticalScrollBarEnabled(false);
        _posts.setVerticalFadingEdgeEnabled(false);
        _posts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        _posts.setDividerHeight(1);
        _posts.setOnItemClickListener(this);
        registerForContextMenu(_posts);
        
        _handler = new PostsHandler<Post>(this, PostsActivity.getSpinner(), _posts, _listOfPosts);
        
        _postsType = getIntent().getStringExtra("post_type");
        if (_postsType.equals("all"))
            _fetchPostsThread = new Thread(new AllRunnable(this, _handler, _listOfPosts, null));
        else
            _fetchPostsThread = new Thread(new PostsRunnable(this, _handler, _listOfPosts, null, _postsType));
        _fetchPostsThread.start();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = new Intent(getApplicationContext(), PostActivity.class);
        Post post = _handler.getAdapter().getViewObject(position);
        postIntent.putExtra("post", post);
        postIntent.putExtra("post_user_icon", ImageUtils.getByteArrayFromBitmap(post.getUserIcon()));
        startActivity(postIntent);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(0, Menu.FIRST, 0, "View Profile");
        menu.add(0, Menu.FIRST + 1, 0, "View Comments");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Post post = _handler.getAdapter().getViewObject(info.position);

        switch(item.getItemId()) {
            case Menu.FIRST:
                User user = post.getUser();
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                userIntent.putExtra("user", user);
                userIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(user.getUserIcon()));
                startActivity(userIntent);
                break;
            case Menu.FIRST + 1:
                Intent comments = new Intent(PostsListActivity.this, CommentsActivity.class);
                comments.putExtra("post_id", post.getId());
                startActivity(comments);
        }
        
        return super.onContextItemSelected(item);
    }

}