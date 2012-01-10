package com.kampr.handlers;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kampr.runnables.SnapRunnable;

public class SnapHandler extends AbstractHandler {
    
    private ImageView _postOriginal;
    
    public SnapHandler(Context context, ProgressBar spinner, ImageView postOriginal) {
        _context = context;
        _spinner = spinner;
        _postOriginal = postOriginal;
    }
    
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _postOriginal.setImageBitmap(SnapRunnable.getSnap());
                _spinner.setVisibility(View.GONE);
                break;
        }
    }

}
