package com.pritam.newsapi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pritam.newsapi.utility.Utility;


public class NewsWebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);

        String URL = getIntent().getStringExtra("url").toString();
        if (Utility.isConnected(this)) {
        WebView WebView = (WebView) findViewById(R.id.web);
        WebView.setWebViewClient(new MyBrowser());
        WebView.getSettings().setLoadsImagesAutomatically(true);
        WebView.getSettings().setJavaScriptEnabled(true);
        WebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebView.loadUrl(URL);
        } else {
            Toast.makeText(this, getResources().getString(R.string.netErr), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

