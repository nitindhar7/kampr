package com.kampr.models;

import java.util.HashMap;
import java.util.Map;

public class Link {
    
    private Map<String, String> _linkProperties;
    
    public Link() {
        _linkProperties = new HashMap<String, String>();
        _linkProperties.put("id", null);
        _linkProperties.put("post_type", null);
        _linkProperties.put("post_url", null);
        _linkProperties.put("created_at", null);
        _linkProperties.put("name", null);
        _linkProperties.put("title", null);
        _linkProperties.put("url", null);
        _linkProperties.put("content", null);
        _linkProperties.put("description", null);
        _linkProperties.put("formatted_content", null);
        _linkProperties.put("formatted_description", null);
        _linkProperties.put("view_count", null);
        _linkProperties.put("like_count", null);
        _linkProperties.put("comment_count", null);
        _linkProperties.put("tag_string", null);
    }
    
    public Link(Map<String, String> linkProperties) {
        _linkProperties = linkProperties;
    }
    
    public Map<String, String> getLinkProperties() {
        return _linkProperties;
    }
    
    public String getLinkProperty(String property) {
        return _linkProperties.get(property);
    }
    
    public void setLinkProperties(Map<String, String> linkProperties) {
        _linkProperties = linkProperties;
    }
    
    public void setLinkProperty(String property, String value) {
        _linkProperties.put(property, value);
    }

}
