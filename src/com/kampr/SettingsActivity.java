package com.kampr;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kampr.adapters.SettingsAdapter;
import com.markupartist.android.widget.ActionBar;

public class SettingsActivity extends ListActivity implements OnItemClickListener {
    
    private final String ACTIVITY_TAG = "SettingsActivity";
    
    private SettingsAdapter _settingsAdapter;
    private ListView _settings;
    private ActionBar _actionBar;
    private List<String> _settingsList;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.settings);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        _settings = getListView();
        _settings.setVerticalScrollBarEnabled(false);
        _settings.setVerticalFadingEdgeEnabled(false);
        _settings.setCacheColorHint(R.color.transparent);
        _settings.setDivider(getResources().getDrawable(R.color.setting_item_divider));
        _settings.setDividerHeight(1);
        _settings.setOnItemClickListener(this);
        
        _settingsList = new ArrayList<String>();
        _settingsList.add("Notifications");
        _settingsList.add("Posts Homepage");
        _settingsList.add("Terms of Use");
        _settingsList.add("Logout");
        
        _settingsAdapter = new SettingsAdapter(SettingsActivity.this, _settingsList);
        _settings.setAdapter(_settingsAdapter);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

}
