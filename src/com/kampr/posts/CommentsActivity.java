package com.kampr.posts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kampr.KamprActivity;
import com.kampr.LogoutActivity;
import com.kampr.R;
import com.kampr.handlers.CommentsHandler;
import com.kampr.models.Comment;
import com.kampr.runnables.CommentsRunnable;
import com.kampr.util.KamprImageUtils;
import com.kampr.util.KamprUtils;

public class CommentsActivity extends ListActivity {

    private final int LOGOUT_RESULT_CODE = 1;
    
    protected static SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private TextView _postTitleView;
    private TextView _postUsernameView;
    private TextView _postDateView;
    private ImageView _postUserIconView;
    
    private int _postId;
    private String _postTitle;
    private String _postName;
    private String _postCreatedAt;
    private Bitmap _postUserIcon;

    private ListView _comments;
    private ProgressDialog _dialog;
    private List<Comment> _listOfComments;
    private Map<String,Bitmap> _userIcons;
    private CommentsHandler _handler;
    private Thread _fetchCommentsThread;
    
    public CommentsActivity() {
        _listOfComments = new ArrayList<Comment>();
        _userIcons = new HashMap<String,Bitmap>();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        KamprUtils.trustAllHosts();

        _postId = Integer.parseInt(getIntent().getStringExtra("post_id"));
        _postTitle = getIntent().getStringExtra("post_title");
        _postName = getIntent().getStringExtra("post_name");
        _postCreatedAt = getIntent().getStringExtra("post_created_at");
        _postUserIcon = KamprImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        
        _postTitleView = (TextView) findViewById(R.id.post_title);
        _postUsernameView = (TextView) findViewById(R.id.post_user_name);
        _postDateView = (TextView) findViewById(R.id.post_date);
        _postUserIconView = (ImageView) findViewById(R.id.post_user_icon);
        
        _postTitleView.setText(_postTitle);
        _postUsernameView.setText(_postName);
        _postDateView.setText(_postCreatedAt);
        _postUserIconView.setImageBitmap(_postUserIcon);
        
        _comments = getListView();
        _comments.setVerticalScrollBarEnabled(false);
        _comments.setDivider(getResources().getDrawable(R.color.comment_item_divider));
        _comments.setDividerHeight(1);

        _dialog = ProgressDialog.show(CommentsActivity.this, "", "Loading...", true);
        
        _handler = new CommentsHandler(this, _dialog, _comments, _listOfComments, _userIcons);
        _fetchCommentsThread = new Thread(new CommentsRunnable(this, _handler, _listOfComments, _userIcons, _postId));
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
