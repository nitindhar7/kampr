package com.kampr.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kampr.R;
import com.kampr.adapters.PostsAdapter;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.util.NetworkUtils;

public abstract class PostsListActivity<T> extends ListActivity implements OnItemClickListener {

    protected ListView _posts;
    protected ProgressDialog _dialog;
    protected Map<String,Bitmap> _userIcons;
    protected List<PropertyContainer> _listOfPosts;
    protected Thread _fetchPostsThread;
    protected PostsHandler<T> _handler;
    protected PostsAdapter<T> _postsAdapter;
    
    public PostsListActivity() {
        NetworkUtils.trustAllHosts();
        _userIcons = new HashMap<String,Bitmap>();
        _listOfPosts = new ArrayList<PropertyContainer>();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _posts = getListView();
        _posts.setVerticalScrollBarEnabled(false);
        _posts.setVerticalFadingEdgeEnabled(false);
        _posts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        _posts.setDividerHeight(1);
        _posts.setOnItemClickListener(this);
        registerForContextMenu(_posts);
        _dialog = ProgressDialog.show(PostsListActivity.this, "", "Loading...", true);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(0, Menu.FIRST, 0, "View Profile");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()) {
            case Menu.FIRST:
                break;
        }
        
        return super.onContextItemSelected(item);
    }

}