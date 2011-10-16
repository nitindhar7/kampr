package com.kampr.adapters;

import java.util.List;

import android.content.Context;

import com.kampr.models.Snap;

public class SnapsAdapter extends PostsAdapter<Snap> {
    
    public SnapsAdapter(Context context, List<Snap> snaps) {
        super(context, snaps);
    }

}
