package com.kampr.tabs;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.kampr.adapters.LinksAdapter;
import com.kampr.models.Link;

public class LinksActivity extends ListActivity {

    private final String ACTIVITY_TAG = "LinksActivity";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        List<Link> listOfLinks = new ArrayList<Link>();
        for(int i = 0; i < 5; i++) {
            Link link = new Link();
            link.setLinkProperty("id", Integer.toString(i));
            link.setLinkProperty("title", "Title " + i);
            link.setLinkProperty("description", "Description " + i);
            listOfLinks.add(link);
        }

        ListView links = getListView();
        LinksAdapter linksAdapter = new LinksAdapter(LinksActivity.this, listOfLinks);
        links.setAdapter(linksAdapter);
    }
    
}
