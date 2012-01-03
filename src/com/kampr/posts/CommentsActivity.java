package com.kampr.posts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kampr.KamprActivity;
import com.kampr.LogoutActivity;
import com.kampr.R;
import com.kampr.handlers.CommentsHandler;
import com.kampr.models.Comment;
import com.kampr.runnables.CommentsRunnable;
import com.kampr.util.NetworkUtils;

public class CommentsActivity extends ListActivity {
    
    protected static SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final int LOGOUT_RESULT_CODE = 1;
    private static final String ACTIVITY_TAG = "Comments";
    
    private TextView _actionbarLogo;
    
    private int _postId;

    private ListView _comments;
    private ProgressDialog _dialog;
    private List<Comment> _listOfComments;
    private CommentsHandler _handler;
    private Thread _fetchCommentsThread;
    
    public CommentsActivity() {
        _listOfComments = new ArrayList<Comment>();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        NetworkUtils.trustAllHosts();

        _postId = Integer.parseInt(getIntent().getStringExtra("post_id"));
        
        _actionbarLogo = (TextView) findViewById(R.id.actionbar_logo);

        _actionbarLogo.setText(ACTIVITY_TAG);
        
        _comments = getListView();
        _comments.setVerticalScrollBarEnabled(false);
        _comments.setVerticalFadingEdgeEnabled(true);
        _comments.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        _comments.setDividerHeight(1);

        _dialog = ProgressDialog.show(CommentsActivity.this, "", "Loading...", true);
        
        _handler = new CommentsHandler(this, _dialog, _comments, _listOfComments);
        _fetchCommentsThread = new Thread(new CommentsRunnable(this, _handler, _listOfComments, _postId));
        _fetchCommentsThread.start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.posts_menu_logout:
                Intent logout = new Intent(CommentsActivity.this, LogoutActivity.class);
                startActivityForResult(logout, LOGOUT_RESULT_CODE);
                break;
        }
        return true;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LOGOUT_RESULT_CODE:
                if(resultCode == LogoutActivity.RESULT_SUCCESS) {
                    Intent kampr = new Intent(CommentsActivity.this, KamprActivity.class);
                    startActivity(kampr);
                }
                else if(resultCode == LogoutActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Error logging out. Try Again!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
