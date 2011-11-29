package com.kampr;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.kampr.adapters.SettingsAdapter;

public class SettingsActivity extends ListActivity implements OnItemClickListener {
    
    private final int LOGOUT_RESULT_CODE = 1;
    
    private SettingsAdapter _settingsAdapter;
    private ListView _settings;
    private List<String> _settingsList;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
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
        
        _settingsAdapter = new SettingsAdapter(SettingsActivity.this, _settingsList);
        _settings.setAdapter(_settingsAdapter);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.posts_menu_logout:
                Intent logout = new Intent(SettingsActivity.this, LogoutActivity.class);
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
                    Intent kampr = new Intent(SettingsActivity.this, KamprActivity.class);
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
