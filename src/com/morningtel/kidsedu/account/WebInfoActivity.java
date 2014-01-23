package com.morningtel.kidsedu.account;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.morningtel.kidsedu.BaseActivity;
import com.morningtel.kidsedu.R;

public class WebInfoActivity extends BaseActivity {
	
	PullToRefreshWebView mPullRefreshWebView=null;
	WebView activity_webview=null;
	ProgressBar web_1_bottom_pb=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);
		
		init();
	}
	
	public void init() {
		
		web_1_bottom_pb=(ProgressBar) findViewById(R.id.web_1_bottom_pb);
		web_1_bottom_pb.setMax(100);
		
		mPullRefreshWebView=(PullToRefreshWebView) findViewById(R.id.activity_webview);
		mPullRefreshWebView.setProgressBar(web_1_bottom_pb);
		activity_webview=mPullRefreshWebView.getRefreshableView();
		//无右侧滚动条
		activity_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		//不使用缓存：
		activity_webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		WebSettings settings=activity_webview.getSettings();
		settings.setJavaScriptEnabled(true);
		activity_webview.addJavascriptInterface(this, "JumpActivity");
		activity_webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				activity_webview.loadUrl("file:///android_asset/web/index.html");
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				web_1_bottom_pb.setVisibility(View.VISIBLE);
				mPullRefreshWebView.setStart();
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				mPullRefreshWebView.setEnd();
			}
			
			@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
				return false;
            }
		});
		settings.setBuiltInZoomControls(false);
		activity_webview.loadUrl("http://www.baidu.com");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			if(activity_webview.canGoBack()) {
				activity_webview.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
