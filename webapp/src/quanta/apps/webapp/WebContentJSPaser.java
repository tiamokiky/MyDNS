package quanta.apps.webapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebContentJSPaser {
	private String TAG="WebContentJSPaser";
	private JSONObject content_json;
	private String JTAG_Name="name",app_name="";
	private String JTAG_URL="url",app_url="";
	private String JTAG_TYPE="type",app_type="";
	WebContentJSPaser(String url){

		String result="";
		try {
			content_json = new JSONObject(result);
			if (content_json.has(JTAG_Name)) {
				app_name = content_json.getString(JTAG_Name);
				Log.d(TAG, "got app_name: " + app_name);	
			} 
			if (content_json.has(JTAG_URL)) {
				app_name = content_json.getString(JTAG_URL);
				Log.d(TAG, "got app_url: " + app_url);	
			}
			if (content_json.has(JTAG_TYPE)) {
				app_name = content_json.getString(JTAG_TYPE);
				Log.d(TAG, "got app_type: " + app_type);	
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "JSONException: " + e.toString());	
		}
	}
	
}
