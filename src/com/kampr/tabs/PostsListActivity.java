package com.kampr.tabs;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kampr.R;
import com.kampr.adapters.PostsAdapter;
import com.kampr.handlers.PostsHandler;

public abstract class PostsListActivity<T> extends ListActivity implements OnItemClickListener, OnScrollListener {

    protected ListView _posts;
    protected ProgressDialog _dialog;
    protected Map<String,Bitmap> _userIcons;
    protected List<T> _listOfPosts;
    protected Thread _fetchPostsThread;
    protected PostsHandler<T> _handler;
    protected PostsAdapter<T> _postsAdapter;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trustAllHosts();

        _userIcons = new HashMap<String,Bitmap>();
        _listOfPosts = new ArrayList<T>();

        _posts = getListView();
        _posts.setVerticalScrollBarEnabled(false);
        _posts.setVerticalFadingEdgeEnabled(false);
        _posts.setDivider(getResources().getDrawable(R.color.post_item_divider));
        _posts.setDividerHeight(1);
        _posts.setOnItemClickListener(this);
        _posts.setOnScrollListener(this);

        _dialog = ProgressDialog.show(PostsListActivity.this, "", "Loading...", true);
    }
    
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        /* TODO
         * 
         * boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
         * if(loadMore) {
         *      _postsAdapter.notifyDataSetChanged();
         * }
         */
    }
    
    public void onScrollStateChanged(AbsListView view, int scrollState) {}
    
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