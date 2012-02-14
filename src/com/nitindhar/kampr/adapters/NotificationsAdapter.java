package com.nitindhar.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.forrst.api.model.Notification;
import com.nitindhar.kampr.R;

public class NotificationsAdapter extends AbstractListAdapter<Notification> {

    public NotificationsAdapter(Context context, List<Notification> notifications) {
        super(context, notifications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.comment_item);
        
        Notification notification = (Notification) _objects.get(position);

        return convertView;
    }
    
}
