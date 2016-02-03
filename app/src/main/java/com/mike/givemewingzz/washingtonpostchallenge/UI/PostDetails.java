package com.mike.givemewingzz.washingtonpostchallenge.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spanned;
import android.transition.TransitionInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mike.givemewingzz.washingtonpostchallenge.R;
import com.mike.givemewingzz.washingtonpostchallenge.customviews.RobotoTextView;
import com.mike.givemewingzz.washingtonpostchallenge.dateutils.DateUtil;
import com.mike.givemewingzz.washingtonpostchallenge.utils.AppConstants;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostDetails extends CoreActivity {

    private static final String TAG = PostDetails.class.getSimpleName();

    @Bind(R.id.post_details_title)
    public RobotoTextView post_title;

    @Bind(R.id.post_details_date)
    public RobotoTextView post_date;

    @Bind(R.id.webview)
    public WebView webView;

    public boolean isSortedByName = false;
    public boolean isSortedByDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.postsinfotransition));
        }

        setContentView(R.layout.post_details_layout);
        ButterKnife.bind(this);

        /*We want to set the menu visibility to none because this activity doesn't require a toolbar*/
        setMenuVisibility(false);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRefreshing(true);
    }

    /* Get information from previous activity and show it accordingly */
    private void initViews() {
        Intent intent = getIntent();
        String content = intent.getStringExtra(AppConstants.CONTENT_KEY);
        String title = intent.getStringExtra(AppConstants.TITLE_KEY);
        String date = intent.getStringExtra(AppConstants.DATE_KEY);

        Calendar calendar = null;
        try {
            calendar = DateUtil.parseISODate(date, TimeZone.getDefault().getDisplayName());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = DateUtil.formatDate(calendar, DateUtil.DateFormat.COMPLETE_DATE_FORMAT);
        Spanned decodedTitle = Html.fromHtml(title);

        post_title.setText(decodedTitle);
        post_date.setText(formattedDate);

        initWebView(content);
        setRefreshing(false);
    }

    /* Initialize the Web view with some custom web settings that's helpful in previewing web data */
    private void initWebView(final String content) {

        WebViewClient wvClient = new BaseWebviewClient(this);
        webView.setWebViewClient(wvClient);

        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        webView.loadDataWithBaseURL("", content, mimeType, encoding, "");

    }

    private class BaseWebviewClient extends WebViewClient {

        private CoreActivity activity;

        public BaseWebviewClient(CoreActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // Display Spinner
            activity.setRefreshing(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setRefreshing(false);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.setVisibility(View.GONE);
            Snackbar.make(container, getString(R.string.error_network), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.orange_5))
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish(); // We want to end the activity if something goes wrong while loading the webview.
                        }
                    }).show();
        }
    }

}
