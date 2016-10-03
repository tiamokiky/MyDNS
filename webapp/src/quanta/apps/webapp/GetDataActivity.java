package quanta.apps.webapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class GetDataActivity extends Activity{
	private String TAG="GetDataActivity";
	Bundle getData;
	String uuid,url,key;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdata);
        getData = getIntent().getExtras();
        if(getData!=null){
        	uuid=getData.getString("uuid", "none");
        	url=getData.getString("url", "none");
        	key=getData.getString("key", "none");
        }
        Toast.makeText(getBaseContext(), "UUID=>"+uuid+", URL=>"+url+",KEY=>"+key, Toast.LENGTH_LONG).show();
	}
}
