package com.kampr.models;

import java.util.HashMap;
import java.util.Map;

public class Post {
    
    protected Map<String, String> _properties;
    
    public Post() {
        _properties = new HashMap<String, String>();
    }
    
    public Post(Map<String, String> properties) {
        _properties = properties;
    }
    
    public Map<String, String> getProperties() {
        return _properties;
    }
    
    public String getProperty(String property) {
        return _properties.get(property);
    }
    
    public void setProperties(Map<String, String> properties) {
        _properties = properties;
    }
    
    public void setProperty(String property, String value) {
        _properties.put(property, value);
    }

}
