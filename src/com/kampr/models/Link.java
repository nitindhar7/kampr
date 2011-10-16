package com.kampr.models;

import java.util.Map;

public class Link extends Post {
    
    public Link() {
        super();
        _properties.put("id", null);
        _properties.put("post_type", null);
        _properties.put("post_url", null);
        _properties.put("created_at", null);
        _properties.put("name", null);
        _properties.put("title", null);
        _properties.put("url", null);
        _properties.put("content", null);
        _properties.put("description", null);
        _properties.put("formatted_content", null);
        _properties.put("formatted_description", null);
        _properties.put("view_count", null);
        _properties.put("like_count", null);
        _properties.put("comment_count", null);
        _properties.put("tag_string", null);
    }
    
    public Link(Map<String, String> properties) {
        super(properties);
    }

}
