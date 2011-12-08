package com.kampr.models;

import java.util.Map;

@SuppressWarnings("serial")
public class Comment extends PropertyContainer {
    
    public Comment() {
        super();
        _properties.put("id", null);
        _properties.put("name", null);
        _properties.put("body", null);
        _properties.put("created_at", null);
        _properties.put("user_photos_thumb_url", null);
    }
    
    public Comment(Map<String, String> properties) {
        super(properties);
    }

}
