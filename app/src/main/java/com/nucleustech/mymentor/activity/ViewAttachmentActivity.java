package com.nucleustech.mymentor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.nucleustech.mymentor.R;

/**
 * Created by ritwik.rai on 20/12/17.
 */

public class ViewAttachmentActivity extends AppCompatActivity {

private WebView wv_attachment;
    ProgressDialog mDialog;
    private Context mContext;
    private String nameUser="";
    private String urlPhotoClick="";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        mContext= ViewAttachmentActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameUser= getIntent().getStringExtra("nameUser");
        getSupportActionBar().setTitle(""+nameUser);
        urlPhotoClick=getIntent().getStringExtra("urlPhotoClick");

        mDialog = new ProgressDialog(mContext);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading! Please wait..");
        mDialog.setTitle(getResources().getString(R.string.app_name));

        wv_attachment= (WebView)findViewById(R.id.wv_attachment);

        if(!TextUtils.isEmpty(urlPhotoClick.trim())) {

            wv_attachment.getSettings().setDomStorageEnabled(true);
            wv_attachment.getSettings().setSaveFormData(true);
            wv_attachment.getSettings().setAllowContentAccess(true);
            wv_attachment.getSettings().setAllowFileAccess(true);
            wv_attachment.getSettings().setAllowFileAccessFromFileURLs(true);
            wv_attachment.getSettings().setAllowUniversalAccessFromFileURLs(true);
            wv_attachment.getSettings().setSupportZoom(true);
            wv_attachment.getSettings().setJavaScriptEnabled(true);
            wv_attachment.getSettings().setSupportMultipleWindows(true);
            wv_attachment.setWebChromeClient(new WebChromeClient());
            wv_attachment.setWebViewClient(new myWebClient());
            wv_attachment.setClickable(true);
            wv_attachment.loadUrl("" + urlPhotoClick.trim());
        }
        else{
            Toast.makeText(mContext,"File cannot be read.",Toast.LENGTH_SHORT).show();
        }

    }

    // ====================================
    // ===== WEB CLIENT ===================

    private class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.v("URL", "" + url);

            mDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mDialog.dismiss();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mDialog.dismiss();

        }
    }
}
