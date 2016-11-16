package com.example.mydns;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String TAG="MyDNS";
	private Button btn_android_version,btn_ip,btn_dns;
	private TextView tv_str;
	private String str_version,str_ip,str_dns;
	Thread t1,t2;
	Handler mHandler = new Handler();
	boolean isRunning = true;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); 
        
        str_version="Android Version is ";
        str_ip="IP is ";
        str_dns="DNS is ";
        tv_str=(TextView) findViewById(R.id.tv_str);;
        btn_android_version=(Button) findViewById(R.id.btn_android_version);
        btn_ip=(Button) findViewById(R.id.btn_ip);
        btn_dns=(Button) findViewById(R.id.btn_dns);
    	t1=new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
            	//getBroadcastAddress();
            	getDNSInfo();
            }
        });
    	t2=new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRunning) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // Write your code here to update the UI.
                                displayData();
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                Log.d(TAG,"T2 died!!");
            }
        }); 

        btn_android_version.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v) {
            	
                // TODO Auto-generated method stub
            	isRunning=false;
            	tv_str.setText(str_version);
            	if (android.os.Build.VERSION.SDK_INT >= 24){
            		Toast.makeText(getApplication(),android.os.Build.VERSION.SDK_INT+">= Nougat 7.0 - 7.1.1", Toast.LENGTH_SHORT).show();
            		
            	}else if (android.os.Build.VERSION.SDK_INT >= 23){
            		Toast.makeText(getApplication(),android.os.Build.VERSION.SDK_INT+">= Marshmallow 6.0 - 6.0.1", Toast.LENGTH_SHORT).show();
            		
            	}else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            		Toast.makeText(getApplication(),android.os.Build.VERSION.SDK_INT+">= Lollipop 5.0 - 5.1.1", Toast.LENGTH_SHORT).show();
            		
            	}else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
            		Toast.makeText(getApplication(),android.os.Build.VERSION.SDK_INT+">= KitKat 4.4 - 4.4.4", Toast.LENGTH_SHORT).show();
    	       
            	}else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
            		Toast.makeText(getApplication(),android.os.Build.VERSION.SDK_INT+">= 4.1 - 4.3.1", Toast.LENGTH_SHORT).show();
    	        }else {
    	        	Toast.makeText(getApplication(), "< 4.1", Toast.LENGTH_SHORT).show();
    	        }
            }

        });
        btn_ip.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v) {

                // TODO Auto-generated method stub
            	isRunning=true;
            	tv_str.setText(str_ip);
            	Toast.makeText(getApplication(),getPhoneIPAddrs(), Toast.LENGTH_SHORT).show();
    	        
            }

        });
        btn_dns.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v) {
            	//t1.start();
            	//t2.start();
            	
            	//getBroadcastAddress();
            	//getDNSInfo();
            	//getDNSInfo_v5();
            	//setDNS();
            	// TODO Auto-generated method stub
            	tv_str.setText(str_dns);

            }

        });
	}
	public void setDNS(){
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE); 
		WifiConfiguration wifiConf = (WifiConfiguration) wifiManager.getConfiguredNetworks(); /* Get Wifi configuration you want to update */

		if (wifiConf != null)
		{
		    try{
		    	 setStaticIpConfiguration(wifiManager, wifiConf,
				 InetAddress.getByName("10.0.0.1"), 24,
				 InetAddress.getByName("10.0.0.2"),
				 new InetAddress[] { InetAddress.getByName("8.8.8.8"), InetAddress.getByName("8.8.4.4") });
		    }catch (Exception e){
				            e.printStackTrace();
		    }
		}
	}
	public void getDNSInfo()
	{
		Log.d(TAG, "----------getDNSInfo-----------");
		
		
		
		
	}
	private void displayData() {
        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            Toast.makeText(this, "Network Available", Toast.LENGTH_SHORT).show();
            //getBroadcastAddress();
            //myTextView.setText("Network Available");
        }
        else
        {
            Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            //myTextView.setText("Network Not Available");
        }       
    }
	public int getBroadcastAddress() 
	{
		Log.d(TAG, "----------getBroadcastAddress-----------");
		WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE); 
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp != null) {
			Log.d(TAG,"dhcp.dns1 (int)=>"+dhcp.dns1);
	        return dhcp.dns1;
	    } else {
	    	Log.d(TAG,"dhcp is NULL");
	        return -1;
	    }
	
	}
	@SuppressWarnings("unchecked")
	private static void setStaticIpConfiguration(WifiManager manager, WifiConfiguration config, InetAddress ipAddress, int prefixLength, InetAddress gateway, InetAddress[] dns) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException
	{
	    // First set up IpAssignment to STATIC.
	    Object ipAssignment = getEnumValue("android.net.IpConfiguration$IpAssignment", "STATIC");
	    callMethod(config, "setIpAssignment", new String[] { "android.net.IpConfiguration$IpAssignment" }, new Object[] { ipAssignment });

	    // Then set properties in StaticIpConfiguration.
	    Object staticIpConfig = newInstance("android.net.StaticIpConfiguration");
	    Object linkAddress = newInstance("android.net.LinkAddress", new Class<?>[] { InetAddress.class, int.class }, new Object[] { ipAddress, prefixLength });

	    setField(staticIpConfig, "ipAddress", linkAddress);
	    setField(staticIpConfig, "gateway", gateway);
	    getField(staticIpConfig, "dnsServers", ArrayList.class).clear();
	    for (int i = 0; i < dns.length; i++)
	        getField(staticIpConfig, "dnsServers", ArrayList.class).add(dns[i]);

	    callMethod(config, "setStaticIpConfiguration", new String[] { "android.net.StaticIpConfiguration" }, new Object[] { staticIpConfig });
	    manager.updateNetwork(config);
	    manager.saveConfiguration();
	}
	private static Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
	{
	    return newInstance(className, new Class<?>[0], new Object[0]);
	}

	private static Object newInstance(String className, Class<?>[] parameterClasses, Object[] parameterValues) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException
	{
	    Class<?> clz = Class.forName(className);
	    Constructor<?> constructor = clz.getConstructor(parameterClasses);
	    return constructor.newInstance(parameterValues);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object getEnumValue(String enumClassName, String enumValue) throws ClassNotFoundException
	{
	    Class<Enum> enumClz = (Class<Enum>)Class.forName(enumClassName);
	    return Enum.valueOf(enumClz, enumValue);
	}

	private static void setField(Object object, String fieldName, Object value) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
	{
	    Field field = object.getClass().getDeclaredField(fieldName);
	    field.set(object, value);
	}

	private static <T> T getField(Object object, String fieldName, Class<T> type) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException
	{
	    Field field = object.getClass().getDeclaredField(fieldName);
	    return type.cast(field.get(object));
	}

	private static void callMethod(Object object, String methodName, String[] parameterTypes, Object[] parameterValues) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
	{
	    Class<?>[] parameterClasses = new Class<?>[parameterTypes.length];
	    for (int i = 0; i < parameterTypes.length; i++)
	        parameterClasses[i] = Class.forName(parameterTypes[i]);

	    Method method = object.getClass().getDeclaredMethod(methodName, parameterClasses);
	    method.invoke(object, parameterValues);
	}
	public List<InetAddress> getDNSInfo_v5()
	{
		 Log.d(TAG, "----------getDNSInfo_v5-----------");
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	    for (Network network : connectivityManager.getAllNetworks()) {
	        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
	        if (networkInfo.isConnected()) {
	            LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
	            Log.d(TAG, "iface = " + linkProperties.getInterfaceName());
	            Log.d(TAG, "dns = " + linkProperties.getDnsServers());
	            return linkProperties.getDnsServers();
	        }
	    }
	    return null;
	}
	
	public String getPhoneIPAddrs()
	{
		Log.d(TAG, "----------getPhoneIPAddrs-----------");
	    String sAddr = "";
	    
	    try
	    {
	        for (Enumeration<NetworkInterface> enumInterfaces = NetworkInterface.getNetworkInterfaces(); enumInterfaces.hasMoreElements(); )
	        {
	            // Get next network interface
	            NetworkInterface mInterface = enumInterfaces.nextElement();
	            
	            for (Enumeration<InetAddress> enumIPAddrs = mInterface.getInetAddresses(); enumIPAddrs.hasMoreElements(); )
	            {
	                // Get next IP address of this interface
	                InetAddress inetAddr = enumIPAddrs.nextElement();
	         
	                // Exclude loopback address
	                if (!inetAddr.isLoopbackAddress())
	                {
	                    if (sAddr != "")
	                    {
	                        sAddr += ", ";
	                    }
	                   
	                    sAddr += "(" + mInterface.getDisplayName() + ") " +  inetAddr.getHostAddress();
	                    
	                  // sAddr += "(" + mInterface.getDisplayName() + ") " +   Formatter.formatIpAddress(inetAddr.getAddress());
	                }
	            }
	        }
	    }
	    catch (SocketException e)
	    {
	        e.printStackTrace();
	    }
	    
	    return sAddr;
	}
}
