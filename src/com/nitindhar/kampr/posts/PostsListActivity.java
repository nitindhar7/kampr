package com.nitindhar.kampr.posts;

import android.app.ListActivity;
import android.content.Intent;
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
import android.widget.Toast;

import com.nitindhar.forrst.model.User;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.UserActivity;
import com.nitindhar.kampr.async.PostsTask;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.NetworkUtils;

public class PostsListActivity<T> extends ListActivity implements OnItemClickListener {

    private static PostsTask _postsTask;
    
    private ListView _posts;
    
    public PostsListActivity() {
        NetworkUtils.trustAllHosts();
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
        
        _postsTask = new PostsTask(this, _posts);
        _postsTask.execute(getIntent().getStringExtra("post_type"));
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = new Intent(getApplicationContext(), PostActivity.class);
        PostDecorator pd = _postsTask.getAdapter().getViewObject(position);
        postIntent.putExtra("post", pd.getPost());
        postIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
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
        PostDecorator pd = _postsTask.getAdapter().getViewObject(info.position);

        switch(item.getItemId()) {
            case Menu.FIRST:
                User user = pd.getPost().getUser();
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                userIntent.putExtra("user", user);
                userIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
                startActivity(userIntent);
                break;
            case Menu.FIRST + 1:
                if(pd.getPost().getCommentCount() > 0) {
                    Intent comments = new Intent(PostsListActivity.this, CommentsActivity.class);
                    comments.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
                    comments.putExtra("post", pd.getPost());
                    startActivity(comments);
                }
                else
                    Toast.makeText(getApplicationContext(), "No comments", Toast.LENGTH_SHORT);
        }
        
        return super.onContextItemSelected(item);
    }

}