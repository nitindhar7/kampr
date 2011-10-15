package com.kampr.tabs;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
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
        
        try {
            List<Link> listOfLinks = new ArrayList<Link>();
            ForrstAPI forrst = new ForrstAPIClient();
            
            JSONObject postsJSON = forrst.postsList("link", null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                JSONObject json = postsJSONArray.getJSONObject(count);
                Link link = new Link();
                link.setLinkProperty("id", json.getString("id"));
                link.setLinkProperty("title", json.getString("title"));
                link.setLinkProperty("description", json.getString("description"));
                listOfLinks.add(link);
            }
            
            ListView links = getListView();
            LinksAdapter linksAdapter = new LinksAdapter(LinksActivity.this, listOfLinks);
            links.setAdapter(linksAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error fetching links from Forrst", e);
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
