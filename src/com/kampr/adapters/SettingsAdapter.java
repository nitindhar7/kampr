package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kampr.R;

public class SettingsAdapter extends BaseAdapter {
    
    protected Context _context;
    protected List<String> _settings;
    
    private LayoutInflater _inflater;
    
    public SettingsAdapter(Context context, List<String> settings) {
        this._context = context;
        this._settings = settings;
        _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        return _settings.size();
    }

    @Override
    public Object getItem(int position) {
        return _settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if(convertView == null) {
            convertView = _inflater.inflate(R.layout.setting_item, null);
        }

        TextView setting = (TextView) getViewHandle(convertView, R.id.setting);
        setting.setText(_settings.get(position));

        return convertView;
    }
    
    protected Object getViewHandle(View parent, int viewId) {
        Object view = parent.getTag(viewId);
        if(view == null) {
            view = parent.findViewById(viewId);
            parent.setTag(viewId, view);
        }
        return view;
    }

}
