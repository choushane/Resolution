package com.realtek.app.resolution;

import static android.net.ethernet.EthernetManager.ETHERNET_STATE_DISABLED;
import static android.net.ethernet.EthernetManager.ETHERNET_STATE_ENABLED;

import android.content.Intent;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.Notification.InboxStyle;
import android.app.ProgressDialog;
import android.os.Bundle;
import com.realtek.hardware.RtkHDMIManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.content.Context;

import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.LinkProperties;
import android.net.LinkAddress;
import android.net.ethernet.IEthernetManager;
import android.net.ethernet.EthernetManager;
import android.net.ethernet.EthernetDevInfo;
import android.net.InterfaceConfiguration;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.WindowManager;

public class ToGoReceiver extends BroadcastReceiver {
	private final static String Resolution = "com.realtek.app.resolution.action.RESOLUTION";
	private final static String Network = "com.realtek.app.resolution.action.NETWORK";
	private final static String Reboot = "com.realtek.app.resolution.action.REBOOT";
	private RtkHDMIManager mRtkHDMIManager;
	private EthernetManager mEthManager;
	private ConnectivityManager mService;
	private EthernetDevInfo mInterfaceInfo;
	private ProgressDialog pd;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Resolution.equals(intent.getAction())) {
			Log.v("Resolution","Enter!!!");
			Log.v("Resolution","go to com.realtek.app.resolution.action.RESOLUTION");
			mRtkHDMIManager = new RtkHDMIManager();
 			Runtime runtime = Runtime.getRuntime();
			int setting_value = 0;
			int read;
			StringBuffer output = new StringBuffer();
			try {
				Process proc = runtime.exec("getprop android.runtime.resolution");
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				char[] buffer = new char[4096];
				while ((read = reader.read(buffer)) > 0) {
					output.append(buffer, 0, read);
				}
				reader.close();	
			} catch (Exception e) {
				Log.v("Resolution","ERROR!!");
			}
				
			if(output.toString().contains("10"))
				setting_value = 10;
			else if(output.toString().contains("2"))
				setting_value = 2;
			else if(output.toString().contains("3"))
				setting_value = 3;
			else if(output.toString().contains("4"))
				setting_value = 4;
			else if(output.toString().contains("5"))
				setting_value = 5;
			else if(output.toString().contains("6"))
				setting_value = 6;
			else if(output.toString().contains("7"))
				setting_value = 7;
			else if(output.toString().contains("8"))
				setting_value = 8;
			else if(output.toString().contains("9"))
				setting_value = 9;
			else if(output.toString().contains("1"))
				setting_value = 1;
			else
				setting_value = 0;
			Log.v("Resolution","setting_value = " + setting_value);
			Settings.System.putInt(context.getContentResolver(),"realtek_setup_tv_system_user" , setting_value);
			mRtkHDMIManager.setTVSystem(setting_value);
		}
		else if (Network.equals(intent.getAction()))
		{
		    Log.e("Network","Enter!!!");

        	    mEthManager = EthernetManager.getInstance();
		    EthernetDevInfo saveInfo = mEthManager.getSavedConfig();

		    if(getprop("qts.net.mode").contains("1")){
			saveInfo.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL);
		    }else
			saveInfo.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_DHCP);

		    if(getprop("qts.net.dns").contains("0.0.0.0"))
			saveInfo.setDnsAddr("8.8.8.8");
		    else
			saveInfo.setDnsAddr(getprop("qts.net.dns"));

		    saveInfo.setIpAddress(getprop("qts.net.ipaddr"));
	 	    saveInfo.setGateWay(getprop("qts.net.gateway"));
		    saveInfo.setNetMask(getprop("qts.net.netmask"));
		    saveInfo.setHwaddr(getprop("qts.net.hwaddr"));

		    mEthManager.updateDevInfo(saveInfo);
		}else if(Reboot.equals(intent.getAction()))
		{
		    pd = new ProgressDialog(context);
		    pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		    pd.setCancelable(false);
		    pd.setTitle(context.getString(com.android.internal.R.string.power_off));
		    pd.setMessage(context.getString(com.android.internal.R.string.shutdown_progress));
		    pd.show();
		}	
    }

	public String getprop(String key) {
		int read;
		Runtime runtime = Runtime.getRuntime();
		StringBuffer output = new StringBuffer();
		try {
			Process proc = runtime.exec("getprop " + key);
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			char[] buffer = new char[4096];
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();	
		} catch (Exception e) {
			Log.v("NETWORK","ERROR!!");
		}
		return output.toString();
	}
}

