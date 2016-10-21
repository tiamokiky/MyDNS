package quanta.apps.webapp;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.quanta.fl1.mohublib.XWalkViewFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends Activity{
	private String TAG="WebAPPMainActivity";
	private Bundle b_arg=new Bundle();
	private XWalkViewFragment xWalkViewFragment = XWalkViewFragment.newInstance();
	private String asset_url="/android_asset/qoca/index.html";
	private String str_data="{\"id\":\"qocaiOPbE8xLPuz\",\"protocol\":\"baycro.O8lO2kGfJQ\",\"name\":\"news?postId=SnauVAw8LY\"}"; //work
	//private String str_data="{\"id\":\"qocaiOPbE8xLPuz\",\"protocol\":\"baycro.4x90Mxnx4H\",\"name\":\"news?postId=HQ2WL1D7sL\"}";
	//private String str_data="{id:\"qocaiOPbE8xLPuz\",protocol:\"baycro.4x90Mxnx4H\",name:\"news?postId=HQ2WL1D7sL\"}"; //work
	private String key,uuid,url;
	private JSONObject content_json;
	private WebView webview=null;	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
	     int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
	     decorView.setSystemUiVisibility(uiOptions);
	     // Remember that you should never show the action bar if the
	     // status bar is hidden, so hide that too if necessary.
	     ActionBar actionBar = getActionBar();
	     actionBar.hide();
         setContentView(R.layout.activity_main2);
        // Create a new Fragment to be placed in the activity layout
        /*
         try {
			//content_json=new JSONObject(str_data);
        	 //Log.d(TAG,"content_json=>"+content_json.toString());
        	 Log.d(TAG,"-----JSONObject Rule----");
        	 content_json=new JSONObject();
        	 content_json.put("protocol", "baycro.4x90Mxnx4H");
        	 content_json.put("name", "news?postId=SnauVAw8LY");
        	 Log.d(TAG,"content_json getString:protocol=>"+content_json.getString("protocol"));
        	 Log.d(TAG,"content_json getString:name=>"+content_json.getString("name"));
        	 Log.d(TAG,"content_json toString=>"+content_json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			 Log.d(TAG,"JSONException e=>"+e.toString());
			e.printStackTrace();
		}
		*/

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        
        Bundle b_data=getIntent().getExtras();
        if(b_data!=null){
        	Log.d(TAG,"-----Original Data----");
	        uuid=b_data.getString("uuid","");
	        key=b_data.getString("key","");
	        url=b_data.getString("url","");
	        b_arg.putString("userId",  uuid);
	        Log.d(TAG,"key=>"+key);
	        Log.d(TAG,"url=>"+url);
	        Log.d(TAG,"uuid=>"+uuid);
	       
        }
        
        
        if(!key.equals("")){ 
        	//Open webAPP and create a JSON data for passing to XWalkView
        	Log.d(TAG,"-----Open WebApp----");
            Log.d(TAG,"key=>"+key);
        	JSONObject mJson=str_to_json(key.replaceAll("\"", ""));
        	Log.d(TAG,"mJson=>"+mJson.toString());
        	
        	String protocol="";
        	try {
				protocol=mJson.getString("protocol");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	if(protocol.startsWith("\"")&&protocol.endsWith("\""))
        		protocol=protocol.substring(1,protocol.length()-1);
        	Log.d(TAG,"protocol=>"+protocol);
        	
        	asset_url="/android_asset/"+protocol+"/index.html";
        	Log.d(TAG,"asset_url=>"+asset_url);
        	
        	 b_arg.putString("url", asset_url); 
             //b_arg.putString("appId",  "qocaiOPbE8xLPuz"); // Old way: Looking for right folder
             Bundle b_temp=new Bundle();
             // b_temp.putString("data", str_data); // for testing
             b_temp.putString("data", mJson.toString());
             b_arg.putBundle("bundleData",  b_temp);

             xWalkViewFragment.setArguments(b_arg);
             if (findViewById(R.id.fragment_container) != null) {
             	// Add the fragment to the 'fragment_container' FrameLayout
             	getFragmentManager().beginTransaction().add(R.id.fragment_container, xWalkViewFragment).commit();
             }
        }else{
        	// Open Browser Apps which are installed in devices
        	Log.d(TAG,"-----Browser WebApp----");
        	if(url.startsWith("http://")||url.startsWith("https://")){      		
        		webview= new WebView(this);
            	webview.loadUrl(url);
            	finish();
        	}else{
        		Log.d(TAG,"-----Check Url----");
     	        Log.d(TAG,"url=>"+url);
        	}
        	
        
        }
       
        
	}
	private JSONObject str_to_json(String str){
		content_json=new JSONObject();
		if(str.startsWith("{")&&str.endsWith("}")){
			str=str.substring(1,str.length()-1);
		} 
		String temp[]=str.split(",");
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
		return content_json;
	}
}
