package com.kampr.models;

import java.util.HashMap;
import java.util.Map;

public class Snap {
    
    private Map<String, String> _snapProperties;
    
    public Snap() {
        _snapProperties = new HashMap<String, String>();
        _snapProperties.put("id", null);
        _snapProperties.put("post_type", null);
        _snapProperties.put("post_url", null);
        _snapProperties.put("created_at", null);
        _snapProperties.put("name", null);
        _snapProperties.put("title", null);
        _snapProperties.put("url", null);
        _snapProperties.put("content", null);
        _snapProperties.put("description", null);
        _snapProperties.put("formatted_content", null);
        _snapProperties.put("formatted_description", null);
        _snapProperties.put("view_count", null);
        _snapProperties.put("like_count", null);
        _snapProperties.put("comment_count", null);
        _snapProperties.put("tag_string", null);
        _snapProperties.put("snaps_original_url", null);
    }
    
    public Snap(Map<String, String> snapProperties) {
        _snapProperties = snapProperties;
    }
    
    public Map<String, String> getSnapProperties() {
        return _snapProperties;
    }
    
    public String getSnapProperty(String property) {
        return _snapProperties.get(property);
    }
    
    public void setSnapProperties(Map<String, String> snapProperties) {
        _snapProperties = snapProperties;
    }
    
    public void setSnapProperty(String property, String value) {
        _snapProperties.put(property, value);
    }

}
