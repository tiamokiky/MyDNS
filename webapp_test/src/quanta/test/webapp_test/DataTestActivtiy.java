package quanta.test.webapp_test;

import com.example.webapp_test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DataTestActivtiy extends Activity implements OnClickListener {
	 
	
		 
	    Button btnStartWebapp;
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_datatest);
	  						
	        btnStartWebapp = (Button) findViewById(R.id.btnStartWebapp);
	 
	        btnStartWebapp.setOnClickListener(this);
	    }
	 
	 
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			 //calling an activity using <intent-filter> action name 
	        Intent intent = new Intent("quanta.apps.webapp.GetDataActivity");
	        Bundle sendData=new Bundle();
	        sendData.putString("uuid", "uuid_value");
	        sendData.putString("url", "url_value");
	        sendData.putString("key", "key_value");
	  	    intent.putExtras(sendData);
	        startActivity(intent);
		}
	 
}
