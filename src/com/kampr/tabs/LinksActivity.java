package com.kampr.tabs;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.adapters.LinksAdapter;
import com.kampr.models.Link;

public class LinksActivity extends ListActivity {

    private final String ACTIVITY_TAG = "LinksActivity";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        trustAllHosts();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            List<Link> listOfLinks = new ArrayList<Link>();
            ForrstAPI forrst = new ForrstAPIClient();
            
            JSONObject postsJSON = forrst.postsList("link", null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                Map<String, String> linkProperties = new HashMap<String, String>();
                JSONObject json = postsJSONArray.getJSONObject(count);
                
                linkProperties.put("id", json.getString("id"));
                linkProperties.put("post_type", json.getString("post_type"));
                linkProperties.put("post_url", json.getString("post_url"));

                long linkDateInMillis = sdf.parse(json.getString("created_at")).getTime();
                String linkDate = DateUtils.formatDateTime(null, linkDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                linkProperties.put("created_at", linkDate);
                
                linkProperties.put("name", json.getJSONObject("user").getString("name"));
                linkProperties.put("title", json.getString("title"));
                linkProperties.put("url", json.getString("url"));
                linkProperties.put("content", json.getString("content"));
                linkProperties.put("description", json.getString("description"));
                linkProperties.put("formatted_content", json.getString("formatted_content"));
                linkProperties.put("formatted_description", json.getString("formatted_description"));
                linkProperties.put("view_count", Integer.toString(json.getInt("view_count")));
                linkProperties.put("like_count", json.getString("like_count"));
                linkProperties.put("comment_count", json.getString("comment_count"));
                linkProperties.put("tag_string", json.getString("tag_string"));
                
                Link link = new Link(linkProperties);
                listOfLinks.add(link);
            }
            
            ListView links = getListView();
            LinksAdapter linksAdapter = new LinksAdapter(LinksActivity.this, listOfLinks);
            links.setAdapter(linksAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error fetching links from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error parsing link date", e);
        }
    }

    /**
     * Trust every server - dont check for any certificate
     * 1. Create a trust manager that does not validate certificate chains
     * 2. Install the all-trusting trust manager
     */
     protected static void trustAllHosts() {
         TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                 return new java.security.cert.X509Certificate[] {};
             }
         
             @Override
             public void checkClientTrusted(X509Certificate[] chain, String authType) {}
         
             @Override
             public void checkServerTrusted(X509Certificate[] chain, String authType) {}
         }};

         SSLContext sc;
         try {
             sc = SSLContext.getInstance("TLS");
             sc.init(null, trustAllCerts, new java.security.SecureRandom());
             HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
         } catch (NoSuchAlgorithmException e) {
             throw new RuntimeException("Error installing all-trusting trust manager: algorithm not found", e);
         } catch (KeyManagementException e) {
             throw new RuntimeException("Error installing all-trusting trust manager: problems managing key", e);
         }
     }
    
}
