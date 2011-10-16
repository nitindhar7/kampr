package com.kampr.adapters;

import java.util.List;

import android.content.Context;

import com.kampr.models.Link;

public class LinksAdapter extends PostsAdapter<Link> {
    
    public LinksAdapter(Context context, List<Link> links) {
        super(context, links);
    }

}
