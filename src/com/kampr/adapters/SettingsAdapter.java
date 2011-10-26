package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kampr.R;

public class SettingsAdapter extends AbstractListAdapter<String> {
    
    public SettingsAdapter(Context context, List<String> settings) {
        super(context, settings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.setting_item);

        TextView setting = (TextView) getViewHandle(convertView, R.id.setting);
        setting.setText(_objects.get(position));

        return convertView;
    }

}
