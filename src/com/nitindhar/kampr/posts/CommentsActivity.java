package com.nitindhar.kampr.posts;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.forrst.model.User;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.UserActivity;
import com.nitindhar.kampr.async.CommentsTask;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.NetworkUtils;
import com.nitindhar.kampr.util.SpanUtils;

public class CommentsActivity extends ListActivity implements OnClickListener {

    private static CommentsTask commentsTask;
    private static ProgressBar spinner;

    private Post _post;
    private Bitmap _userIcon;
    private ListView _comments;
    private TextView _postViews;
    private TextView _postLikes;
    private TextView _commentHeaderTitle;
    private TextView _postUserName;
    private ImageView _userIconThumbnail;
    private RelativeLayout _commentsHeader;
    private RelativeLayout _commentInfobar;

    public CommentsActivity() {
        NetworkUtils.trustAllHosts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);

        _post = (Post) getIntent().getSerializableExtra("post");
        _userIcon = ImageUtils.getBitmapFromByteArray(getIntent()
                .getByteArrayExtra("user_icon"));

        _postViews = (TextView) findViewById(R.id.post_view_count_label);
        _postLikes = (TextView) findViewById(R.id.post_like_count_label);
        _commentHeaderTitle = (TextView) findViewById(R.id.comment_header_title);
        _postUserName = (TextView) findViewById(R.id.post_user_name);
        _userIconThumbnail = (ImageView) findViewById(R.id.user_icon_thumbnail);
        _commentsHeader = (RelativeLayout) findViewById(R.id.comments_header);
        _commentInfobar = (RelativeLayout) findViewById(R.id.comment_infobar);

        _commentsHeader.setOnClickListener(this);
        _commentInfobar.setOnClickListener(this);

        _comments = getListView();
        _comments.setVerticalScrollBarEnabled(false);
        _comments.setVerticalFadingEdgeEnabled(true);
        _comments.setDivider(this.getResources().getDrawable(
                R.color.post_item_divider));
        _comments.setDividerHeight(1);

        _postViews.setText(Integer.toString(_post.getViewCount()));
        _postLikes.setText(Integer.toString(_post.getLikeCount()));
        _postUserName.setText(_post.getUser().getUsername());
        _userIconThumbnail.setImageBitmap(_userIcon);

        commentsTask = new CommentsTask(this, _comments);
        commentsTask.execute(_post.getId());

        SpanUtils.setFont(getApplicationContext(), _postViews);
        SpanUtils.setFont(getApplicationContext(), _postLikes);
        SpanUtils.setFont(getApplicationContext(), _commentHeaderTitle,
                SpanUtils.FONT_BOLD);
        SpanUtils.setFont(getApplicationContext(), _postUserName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.comments_header:
            finish();
            break;
        case R.id.comment_infobar:
            User user = _post.getUser();
            Intent userIntent = new Intent(getApplicationContext(),
                    UserActivity.class);
            userIntent.putExtra("user", user);
            userIntent.putExtra("user_icon",
                    ImageUtils.getByteArrayFromBitmap(_userIcon));
            startActivity(userIntent);
            break;
        }
    }

    public static ProgressBar getSpinner() {
        return spinner;
    }

}