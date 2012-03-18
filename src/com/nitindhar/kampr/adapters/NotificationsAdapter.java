package com.nitindhar.kampr.adapters;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitindhar.forrst.model.Notification;
import com.nitindhar.kampr.R;

public class NotificationsAdapter extends AbstractListAdapter<Notification> {

    private List<Bitmap> _userIcons;
    
    public NotificationsAdapter(Context context, List<Notification> notifications, List<Bitmap> userIcons) {
        super(context, notifications);
        _userIcons = userIcons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.notification_item);
        
        Notification notification = (Notification) _objects.get(position);
        
        ImageView notificationUserIcon = (ImageView) getViewHandle(convertView, R.id.user_icon_thumbnail);
        notificationUserIcon.setImageBitmap(_userIcons.get(position));

        TextView notificationUsername = (TextView) getViewHandle(convertView, R.id.notification_item_username);
        notificationUsername.setText(notification.getData().getActor());

        // FIXME: forrst lib is not getting the correct notification timestamp
//        TextView notificationDate = (TextView) getViewHandle(convertView, R.id.notification_item_date);
//        notificationDate.setText(TimeUtils.getPostDate(notification.getTimestamp()));

        TextView notificationTitle = (TextView) getViewHandle(convertView, R.id.notification_item_content);
        StringBuilder sb = new StringBuilder(notification.getBehavior().replace('_', ' '));
        
        if(!notification.getBehavior().equals("new_follow")) {
            sb.append(" - ");
            sb.append(notification.getData().getPostTitle());
        }
        notificationTitle.setText(WordUtils.capitalize(sb.toString()));

        convertView.setTag(notification.getId());
        
        // TODO: set fonts for notification item

        return convertView;
    }
    
}
