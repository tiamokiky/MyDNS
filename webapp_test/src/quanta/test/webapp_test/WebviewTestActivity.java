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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class WebviewTestActivity extends Activity {
	String TAG="WebviewTestActivity";
	LinearLayout ll_base;
    WebView mWebView = null;
    NewWebview webview=null;
    Activity activity=null;
    //SwipeRefreshLayout swipeContainer;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_webviewtest);
			activity=this;
			initView();
			//initView2();
			/* swipeContainer.setOnRefreshListener(new OnRefreshListener() {
		            @Override
		            public void onRefresh() {
		                // Your code to refresh the list here.
		                // Make sure you call swipeContainer.setRefreshing(false)
		                // once the network request has completed successfully.
		                fetchTimelineAsync(0);
		            } 
		        });
		        // Configure the refreshing colors
		        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, 
		                android.R.color.holo_green_light, 
		                android.R.color.holo_orange_light, 
		                android.R.color.holo_red_light);*/
		

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
			ViewTreeObserver viewTreeObserver  = mWebView.getViewTreeObserver();

			viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			                   @Override
			                   public boolean onPreDraw() {                                
			                           int height = mWebView.getMeasuredHeight();
			                           if( height != 0 ){
			                                   Toast.makeText(activity, "height:"+height,Toast.LENGTH_SHORT).show();
			                                   mWebView.getViewTreeObserver().removeOnPreDrawListener(this);
			                           }
			                           return false;
			                   }
			           });
			  // disable scroll on touch
			mWebView.setOnTouchListener(new View.OnTouchListener() {
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			      return (event.getAction() == MotionEvent.ACTION_MOVE);
			    }
			  });
			mWebView.setWebViewClient(new WebViewClient());
			//mWebView.setWebChromeClient(new WebChromeClient());
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				mWebView.setWebContentsDebuggingEnabled(true);
			}
			mWebView.setScrollContainer(false);
			mWebView.setVerticalScrollBarEnabled(false);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setAppCacheEnabled(false);
			mWebView.getSettings().setAllowFileAccess(true);
			mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
			}

			//mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
		    //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			
			//mWebView.loadUrl("http://ey5.parseapp.com/");			
			//mWebView.loadUrl("https://tiamokiky.github.io/StaticWeb/");
		    //mWebView.loadUrl("file:///android_asset/myweb/myweb.html");
		    
		   // mWebView.loadUrl("file:///android_asset/myweb/myweb.html");
		   // mWebView.loadUrl("file:///android_asset/my_www/myweb_slide.html");
		    mWebView.loadUrl("https://tiamokiky.github.io/webapp/");
		    //mWebView.loadUrl("http://www.yahoo.com");		
			
			//mWebView.loadUrl("https://demohome.qoca.net/portal");	
			mWebView.addJavascriptInterface(new JsObject(), "JSInterface");
		}
		/* public void fetchTimelineAsync(int page) {
		        // Send the network request to fetch the updated data
		        // `client` here is an instance of Android Async HTTP
		        // getHomeTimeline is an example endpoint.

		        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
		            public void onSuccess(JSONArray json) {
		                // Remember to CLEAR OUT old items before appending in the new ones
		                adapter.clear();
		                // ...the data has come back, add new items to your adapter...
		                adapter.addAll(...);
		                // Now we call setRefreshing(false) to signal refresh has finished
		                swipeContainer.setRefreshing(false);
		            }

		            public void onFailure(Throwable e) {
		                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
		            }
		        });
		    }*/

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
