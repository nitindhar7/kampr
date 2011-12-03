package com.kampr.posts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.kampr.R;

public class SnapFullscreenActivity extends Activity implements OnClickListener {
    
    private ImageView _snapImage;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_fullscreen);
        
        _snapImage = (ImageView) findViewById(R.id.snap_fullscreen_image);
        _snapImage.setImageBitmap(SnapActivity.getBitmap());
        _snapImage.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        finish();
    }

}
