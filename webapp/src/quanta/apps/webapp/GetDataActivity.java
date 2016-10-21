package quanta.apps.webapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GetDataActivity extends Activity{
	private String TAG="GetDataActivity";
	Bundle getData;
	String default_value="none";
	String uuid,url,key;
	public static final int requestNum = 0;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdata);
        getData = getIntent().getExtras();
        if(getData!=null){
        	uuid=getData.getString("uuid", default_value);
        	url=getData.getString("url", default_value);
        	key=getData.getString("key", default_value);
        }
        Toast.makeText(getBaseContext(), "UUID=>"+uuid+", URL=>"+url+",KEY=>"+key, Toast.LENGTH_LONG).show();
        if(!key.equals(default_value)){
        	Intent intent = new Intent();
        	intent.setClass(GetDataActivity.this, MainActivity.class);
        	intent.putExtras(getData);
        	
            startActivityForResult(intent, requestNum);
        	
        }else{
        	
        }
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == requestNum){
               Log.d(TAG,"Result OK");
            }
        }
    }
	
}
