package com.kampr;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.kampr.tabs.CodesActivity;
import com.kampr.tabs.LinksActivity;
import com.kampr.tabs.QuestionsActivity;
import com.kampr.tabs.SnapsActivity;

public class PostsActivity extends TabActivity /*implements OnClickListener*/ {

    private static final LinearLayout.LayoutParams _params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    
    private final int LOGOUT_RESULT_CODE = 1;

    private final int TAB_LINKS_ID = 100;
    private final int TAB_SNAPS_ID = 101;
    private final int TAB_CODES_ID = 102;
    private final int TAB_QUESTIONS_ID = 103;
    
    private static LinearLayout _linkTabLayout;
    private static LinearLayout _snapTabLayout;
    private static LinearLayout _codeTabLayout;
    private static LinearLayout _questionTabLayout;
    
    private ImageView _linkTab;
    private ImageView _snapTab;
    private ImageView _codeTab;
    private ImageView _questionTab;
    
    public PostsActivity() {
        _params.setMargins(5, 0, 5, 0);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        _linkTabLayout = new LinearLayout(this);
        _linkTabLayout.setBackgroundResource(R.drawable.tab);
        _linkTabLayout.setLayoutParams(_params);
        _linkTabLayout.setPadding(30, 0, 30, 0);
        
        _snapTabLayout = new LinearLayout(this);
        _snapTabLayout.setBackgroundResource(R.drawable.tab);
        _snapTabLayout.setLayoutParams(_params);
        _snapTabLayout.setPadding(30, 0, 30, 0);
        
        _codeTabLayout = new LinearLayout(this);
        _codeTabLayout.setBackgroundResource(R.drawable.tab);
        _codeTabLayout.setLayoutParams(_params);
        _codeTabLayout.setPadding(30, 0, 30, 0);
        
        _questionTabLayout = new LinearLayout(this);
        _questionTabLayout.setBackgroundResource(R.drawable.tab);
        _questionTabLayout.setLayoutParams(_params);
        _questionTabLayout.setPadding(30, 0, 30, 0);

        _linkTab = new ImageView(this);
        _linkTab.setImageResource(R.drawable.ic_tab_link_nouveau);
        _linkTab.setId(TAB_LINKS_ID);
        //_linkTab.setOnClickListener(this);
        _linkTabLayout.addView(_linkTab);
        intent = new Intent(PostsActivity.this, LinksActivity.class);
        spec = tabHost.newTabSpec("links").setIndicator(_linkTabLayout).setContent(intent);
        tabHost.addTab(spec);
        
        _snapTab = new ImageView(this);
        _snapTab.setImageResource(R.drawable.ic_tab_snap_nouveau);
        _snapTab.setId(TAB_SNAPS_ID);
        //_snapTab.setOnClickListener(this);
        _snapTabLayout.addView(_snapTab);
        intent = new Intent(PostsActivity.this, SnapsActivity.class);
        spec = tabHost.newTabSpec("snaps").setIndicator(_snapTabLayout).setContent(intent);
        tabHost.addTab(spec);
        
        _codeTab = new ImageView(this);
        _codeTab.setImageResource(R.drawable.ic_tab_code_nouveau);
        _codeTab.setId(TAB_CODES_ID);
        //_codeTab.setOnClickListener(this);
        _codeTabLayout.addView(_codeTab);
        intent = new Intent(PostsActivity.this, CodesActivity.class);
        spec = tabHost.newTabSpec("code").setIndicator(_codeTabLayout).setContent(intent);
        tabHost.addTab(spec);
        
        _questionTab = new ImageView(this);
        _questionTab.setImageResource(R.drawable.ic_tab_question_nouveau);
        _questionTab.setId(TAB_QUESTIONS_ID);
        //_questionTab.setOnClickListener(this);
        _questionTabLayout.addView(_questionTab);
        intent = new Intent(PostsActivity.this, QuestionsActivity.class);
        spec = tabHost.newTabSpec("question").setIndicator(_questionTabLayout).setContent(intent);
        tabHost.addTab(spec);
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
                Intent logout = new Intent(PostsActivity.this, LogoutActivity.class);
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
                    Intent kampr = new Intent(PostsActivity.this, KamprActivity.class);
                    startActivity(kampr);
                }
                else if(resultCode == LogoutActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Error logging out. Try Again!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    @Override
//    public void onClick(View view) {
//        switch(view.getId()) {
//        case TAB_LINKS_ID:
//            _linkTab.setImageResource(R.drawable.ic_tab_link_nouveau_selected);
//            break;
//        case TAB_SNAPS_ID:
//            _snapTab.setImageResource(R.drawable.ic_tab_snap_nouveau_selected);
//            break;
//        case TAB_CODES_ID:
//            _codeTab.setImageResource(R.drawable.ic_tab_code_nouveau_selected);
//            break;
//        case TAB_QUESTIONS_ID:
//            _questionTab.setImageResource(R.drawable.ic_tab_question_nouveau_selected);
//            break;
//        }
//    }

}