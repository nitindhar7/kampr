package com.kampr.tabs;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.ListActivity;

public class PostsListActivity extends ListActivity {
    
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