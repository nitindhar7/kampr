package com.nitindhar.kampr.util;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;

import com.nitindhar.kampr.R;

public class LayoutUtils {

    public static void layoutOverride(View view, int visibility) {
        view.setVisibility(visibility);
    }

    public static void createActionBar(Activity activity) {
        createActionBar(activity, false);
    }

    public static void createActionBar(Activity activity, boolean backEnabled) {
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(activity.getResources().getDrawable(
                R.drawable.ic_action_kampr));
        actionBar.setBackgroundDrawable(activity.getResources().getDrawable(
                R.color.kampr_green));
        actionBar.setHomeButtonEnabled(true);
        if(backEnabled) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}