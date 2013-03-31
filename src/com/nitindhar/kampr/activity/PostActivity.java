package com.nitindhar.kampr.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.forrst.model.User;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.async.SnapTask;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.SpanUtils;
import com.nitindhar.kampr.util.TextUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class PostActivity extends Activity implements OnClickListener {

    private static final int LOGOUT_RESULT_CODE = 1;

    private static Post post;

    private RelativeLayout infobar;
    private Bitmap userIconBitmap;
    private TextView postLikes;
    private TextView postViews;
    private TextView postTitle;
    private TextView postUrl;
    private TextView postUsername;
    private TextView postDate;
    private TextView postDescription;
    private TextView postContent;
    private TextView postSnapLabel;
    private ImageView postUserIcon;
    private ImageView postOriginal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.post);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        infobar = (RelativeLayout) findViewById(R.id.post_infobar);
        postTitle = (TextView) findViewById(R.id.post_title);
        postUrl = (TextView) findViewById(R.id.post_url);
        postUsername = (TextView) findViewById(R.id.post_user_name);
        postDate = (TextView) findViewById(R.id.post_date);
        postDescription = (TextView) findViewById(R.id.post_description);
        postUserIcon = (ImageView) findViewById(R.id.user_icon_thumbnail);
        postLikes = (TextView) findViewById(R.id.post_like_count_label);
        postViews = (TextView) findViewById(R.id.post_view_count_label);
        postOriginal = (ImageView) findViewById(R.id.snap_large_url);
        postSnapLabel = (TextView) findViewById(R.id.post_snap_call_to_action);
        postContent = (TextView) findViewById(R.id.post_content);

        postOriginal.setOnClickListener(this);
        infobar.setOnClickListener(this);

        post = (Post) getIntent().getSerializableExtra("post");

        postTitle.setText(post.getTitle().toUpperCase());
        postUsername.setText(post.getUser().getUsername());
        postDate.setText(TimeUtils.getPostDate(post.getCreatedAt()));
        postDescription.setText(TextUtils.cleanseText(post.getDescription()));
        postDescription
                .setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
        postLikes.setText(Integer.toString(post.getLikeCount()));
        postViews.setText(Integer.toString(post.getViewCount()));
        userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent()
                .getByteArrayExtra("user_icon"));
        postUserIcon.setImageBitmap(userIconBitmap);

        if (post.getPostType().equals("link")) {
            postOriginal.setVisibility(View.GONE);
            postContent.setVisibility(View.GONE);
            postSnapLabel.setVisibility(View.GONE);
            postUrl.setText(post.getUrl());
            SpanUtils.removeUnderlines((Spannable) postUrl.getText());
        } else if (post.getPostType().equals("snap")) {
            postUrl.setVisibility(View.GONE);
            postContent.setVisibility(View.GONE);
            SnapTask snapTask = new SnapTask(postOriginal);
            snapTask.execute(post);
        } else if (post.getPostType().equals("code")) {
            postOriginal.setVisibility(View.GONE);
            postUrl.setVisibility(View.GONE);
            postSnapLabel.setVisibility(View.GONE);
            postContent.setText(post.getContent());
            postContent
                    .setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
        } else if (post.getPostType().equals("multipost")) {
            postOriginal.setVisibility(View.GONE);
            postUrl.setVisibility(View.GONE);
            postContent.setVisibility(View.GONE);
            postSnapLabel.setVisibility(View.GONE);
        } else {
            postOriginal.setVisibility(View.GONE);
            postUrl.setVisibility(View.GONE);
            postContent.setVisibility(View.GONE);
            postSnapLabel.setVisibility(View.GONE);
            postDescription.setText(TextUtils.cleanseText(post.getContent()));
            postDescription
                    .setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.snap_large_url:
            Intent fullScreen = new Intent(PostActivity.this,
                    SnapFullscreenActivity.class);
            fullScreen.putExtra("snaps_original_url", post.getSnap()
                    .getOriginalUrl());
            startActivity(fullScreen);
            break;
        case R.id.post_infobar:
            User user = post.getUser();
            Intent userIntent = new Intent(getApplicationContext(),
                    UserActivity.class);
            userIntent.putExtra("user", user);
            userIntent.putExtra("user_icon",
                    ImageUtils.getByteArrayFromBitmap(userIconBitmap));
            startActivity(userIntent);
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case LOGOUT_RESULT_CODE:
            if (resultCode == getResources().getInteger(
                    R.integer.logout_success)) {
                Intent kampr = new Intent(PostActivity.this,
                        KamprActivity.class);
                startActivity(kampr);
            } else if (resultCode == getResources().getInteger(
                    R.integer.logout_failure)) {
                Toast.makeText(getApplicationContext(),
                        "Error logging out. Try Again!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Unexpected error. Try again!", Toast.LENGTH_SHORT)
                        .show();
            }
            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_post_menu, menu);
        return true;
    }

}