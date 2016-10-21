package quanta.test.webapp_test;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.webapp_test.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class WebviewTestActivity extends Activity {
	String TAG="WebviewTestActivity";
	LinearLayout ll_base;
    WebView mWebView = null;
    NewWebview webview=null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_webviewtest);
			initView();
			//initView2();
		}
		private void initView2() {
			ll_base=(LinearLayout) findViewById(R.id.ll_base);
			//webview=(NewWebview) findViewById(R.id.newwebview);
			webview = new NewWebview(this);
			webview.setBackgroundColor(0x00abcdef);
			/*LayoutParams wv_lp=webview.getLayoutParams();
			Log.d(TAG, "Original webview==>>height,width: "+wv_lp.height+","+wv_lp.width );
			wv_lp.height=300;
			wv_lp.width=300;
			webview.setLayoutParams(wv_lp);*/
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int height = displaymetrics.heightPixels;
			int width = displaymetrics.widthPixels;
			Log.d(TAG, "Device==>>height,width: "+height+","+width );
	        WebSettings websettings = webview.getSettings();
	        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
	        {
	            webview.enablecrossdomain41();
	            websettings.setAllowUniversalAccessFromFileURLs(true);
	            websettings.setAllowFileAccessFromFileURLs(true);
	        }
	        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
	        	webview.setWebContentsDebuggingEnabled(true);
			}
	        else
	        {
	            webview.enablecrossdomain();    
	        } 
	        
	    	ll_base.addView(webview,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int) (height*0.8)));
	        
	    	webview.setWebViewClient(new WebViewClient());
	        webview.loadUrl("http://ey5.parseapp.com/");
	      // webview.loadUrl("https://demohome.qoca.net/portal");	
	      //  webview.loadUrl("http://www.yahoo.com");
			//mWebView.loadUrl("http://www.yahoo.com");		
			//mWebView.loadUrl("https://demohome.qoca.net/portal");	
	        webview.addJavascriptInterface(new JsObject(), "JSInterface");
	     
			
		}

		@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
		
		private void initView() {
			mWebView = (WebView)findViewById(R.id.webview);
			
			mWebView.setWebViewClient(new WebViewClient());
			//mWebView.setWebChromeClient(new WebChromeClient());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				mWebView.setWebContentsDebuggingEnabled(true);
			}
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setAppCacheEnabled(false);
			mWebView.getSettings().setAllowFileAccess(true);
			mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
			}

			//mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
		    mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			
			//mWebView.loadUrl("http://ey5.parseapp.com/");			
			//mWebView.loadUrl("https://tiamokiky.github.io/StaticWeb/");
		    //mWebView.loadUrl("file:///android_asset/myweb/myweb.html");
		    
		   // mWebView.loadUrl("file:///android_asset/myweb/myweb.html");
		    mWebView.loadUrl("file:///android_asset/my_www/myweb_slide.html");
		    //mWebView.loadUrl("http://www.yahoo.com");		
			
			//mWebView.loadUrl("https://demohome.qoca.net/portal");	
			mWebView.addJavascriptInterface(new JsObject(), "JSInterface");
		}
		
		class JsObject {
			
			@JavascriptInterface
			public void onStartApp(String dataBundle) {
				Log.d(TAG, "==>>onStartApp: dataBundle>>"+dataBundle );
				JSONObject content_json=new JSONObject();
				if(dataBundle.startsWith("{")&&dataBundle.endsWith("}")){
					dataBundle=dataBundle.substring(1,dataBundle.length()-1);
				} 
				
				String temp[]=dataBundle.split(",");
				if(temp.length>0){
					for(int i=0;i<temp.length;i++){
						String value[]=temp[i].split(":");
						try {
							content_json.put(value[0], value[1]);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Log.d(TAG, "==>>onStartApp: content_json>>"+content_json.toString());
				Intent intent = new Intent("quanta.apps.webapp.GetDataActivity");
			    intent.setAction("webapps.action.MAIN.start");
			    Bundle sendData=new Bundle();
			    sendData.putString("uuid", "uuid_1");
				sendData.putString("url", "http://qoca.baycrest.org");
				// sendData.putString("key", dataBundle);
				sendData.putString("key", content_json.toString());
				intent.putExtras(sendData);
			    startActivity(intent);
			}
		}
		
	}
