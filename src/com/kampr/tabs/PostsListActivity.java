package com.kampr.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kampr.R;
import com.kampr.adapters.PostsAdapter;
import com.kampr.handlers.PostsHandler;
import com.kampr.util.KamprUtils;

public abstract class PostsListActivity<T> extends ListActivity implements OnItemClickListener {

    protected ListView _posts;
    protected ProgressDialog _dialog;
    protected Map<String,Bitmap> _userIcons;
    protected List<T> _listOfPosts;
    protected Thread _fetchPostsThread;
    protected PostsHandler<T> _handler;
    protected PostsAdapter<T> _postsAdapter;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KamprUtils.trustAllHosts();

        _userIcons = new HashMap<String,Bitmap>();
        _listOfPosts = new ArrayList<T>();

        _posts = getListView();
        _posts.setVerticalScrollBarEnabled(false);
        _posts.setVerticalFadingEdgeEnabled(false);
        _posts.setDivider(getResources().getDrawable(R.color.post_item_divider));
        _posts.setDividerHeight(1);
        _posts.setOnItemClickListener(this);

        _dialog = ProgressDialog.show(PostsListActivity.this, "", "Loading...", true);
    }

}