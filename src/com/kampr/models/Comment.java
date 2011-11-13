package com.kampr.models;

import java.util.Map;

public class Comment extends PropertyContainer {
    
    public Comment() {
        super();
        _properties.put("id", null);
        _properties.put("name", null);
        _properties.put("body", null);
        _properties.put("created_at", null);
    }
    
    public Comment(Map<String, String> properties) {
        super(properties);
    }

}
