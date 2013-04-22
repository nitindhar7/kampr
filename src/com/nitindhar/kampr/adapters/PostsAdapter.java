package com.nitindhar.kampr.adapters;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.activity.UserActivity;
import com.nitindhar.kampr.async.CommentsTask;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class PostsAdapter<T> extends AbstractListAdapter<T> {

    public PostsAdapter(Context context, List<T> posts) {
        super(context, posts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.post_item);

        final PostDecorator pd = (PostDecorator) objects.get(position);
        final Post post = pd.getPost();

        OnClickListener onUserClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(context, UserActivity.class);
                userIntent.putExtra("user", post.getUser());
                userIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
                context.startActivity(userIntent);
            }
        };

        ImageView postUserIcon = (ImageView) getViewHandle(convertView,
                R.id.user_icon_thumbnail);
        postUserIcon.setImageBitmap(pd.getUserIcon());
        postUserIcon.setOnClickListener(onUserClickListener);

        TextView postUsername = (TextView) getViewHandle(convertView,
                R.id.post_item_username);
        postUsername.setText(post.getUser().getName());
        postUsername.setOnClickListener(onUserClickListener);

        TextView postDate = (TextView) getViewHandle(convertView,
                R.id.post_item_date);
        postDate.setText(TimeUtils.getPostDate(post.getCreatedAt()));

        TextView postLikes = (TextView) getViewHandle(convertView,
                R.id.post_likes_count);
        postLikes.setText(Integer.toString(post.getLikeCount()));

        TextView postViews = (TextView) getViewHandle(convertView,
                R.id.post_views_count);
        postViews.setText(Integer.toString(post.getViewCount()));

        TextView postComments = (TextView) getViewHandle(convertView,
                R.id.post_comments_count);
        postComments.setText(Integer.toString(post.getCommentCount()));

        TextView postTitle = (TextView) getViewHandle(convertView,
                R.id.post_item_content);
        postTitle.setText(post.getTitle());

        TextView postDescription = (TextView) getViewHandle(convertView,
                R.id.post_item_description);
        postDescription.setText(post.getDescription().trim());
        if(post.getPostType().equals("multipost")) {
            RelativeLayout container = (RelativeLayout) getViewHandle(convertView,
                    R.id.post_item_bottom);
            for(Bitmap snap : pd.getPostSnaps()) {

//                ImageView image = new ImageView(context);
//                image.setImageBitmap(snap);
//                image.setLayoutParams(params);
//                container.addView(image);
            }
        }

        OnClickListener onCountsClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                View commentsFlyout = inflater.inflate(R.layout.comments_flyout, null);
                ListView comments = (ListView) commentsFlyout.findViewById(android.R.id.list);
                comments.setVerticalScrollBarEnabled(false);
                comments.setVerticalFadingEdgeEnabled(false);
                comments.setDivider(context.getResources().getDrawable(R.color.kampr_light_green));
                comments.setDividerHeight(1);

                CommentsTask commentsTask = new CommentsTask(context, comments);
                commentsTask.execute(post.getId());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog alert = builder.create();
                alert.setView(commentsFlyout);
                alert.show();
            }
        };

        RelativeLayout postItemCounts = (RelativeLayout) getViewHandle(convertView,
                R.id.post_item_counts);
        if(post.getCommentCount() > 0) {
            postItemCounts.setOnClickListener(onCountsClickListener);
        }

        convertView.setId(post.getId());

        return convertView;
    }

    public PostDecorator getViewObject(int position) {
        return (PostDecorator) objects.get(position);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

}