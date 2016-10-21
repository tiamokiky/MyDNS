package quanta.test.webapp_test;

import com.example.webapp_test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DataTestActivtiy extends Activity implements OnClickListener {
	 
	private String TAG="DataTestActivtiy";
		 
	    Button btnStartWebapp,btnBrowserWebapp,btnPortal;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_datatest);
	  						
	        btnStartWebapp = (Button) findViewById(R.id.btnStartWebapp);
	        btnBrowserWebapp=(Button) findViewById(R.id.btnBrowserWebapp);
	        btnPortal=(Button) findViewById(R.id.btnPortal);
	        
	        btnStartWebapp.setOnClickListener(this);
	        btnBrowserWebapp.setOnClickListener(this);
	        btnPortal.setOnClickListener(this);
	    }
	 
	 
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			 //calling an activity using <intent-filter> action name
			 Log.d(TAG,"view.id============>"+view.getId());
			Intent intent = new Intent("quanta.apps.webapp.GetDataActivity");
		    intent.setAction("webapps.action.MAIN.start");
		    Bundle sendData=new Bundle();
			if(view.getId()==R.id.btnStartWebapp){
			  Log.d(TAG,"============btnStartWebapp============");
			  sendData.putString("uuid", "uuid_1");
			  sendData.putString("url", "http://qoca.baycrest.org");
			  sendData.putString("key", "{id:qocaiOPbE8xLPuz,protocol:baycro.4x90Mxnx4H,name:news?postId=SnauVAw8LY}");
			  intent.putExtras(sendData);
		        startActivity(intent);
			}else if(view.getId()==R.id.btnBrowserWebapp){
				  Log.d(TAG,"============btnBrowserWebapp============");
				sendData.putString("uuid", "uuid_2");
				sendData.putString("url", "http://yahoo.com");
				sendData.putString("key", "");
				intent.putExtras(sendData);
		        startActivity(intent);
			}else if(view.getId()==R.id.btnPortal){
				Log.d(TAG,"============btnPortal============");
				intent = new Intent();
                intent.setClass(DataTestActivtiy.this, WebviewTestActivity.class);
                startActivity(intent);
			}
	      	       
	  	    
		}
	 
}
