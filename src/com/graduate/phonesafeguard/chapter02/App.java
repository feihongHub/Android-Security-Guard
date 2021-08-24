package com.graduate.phonesafeguard.chapter02;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class App extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//检查SIM卡方法
		correctSIM();
	}
	public void correctSIM(){
		SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
		//获取防盗保护状态
		boolean protecting = sp.getBoolean("protecting", true);
		if(protecting){
			//得到绑定的SIM卡序列号
			String bindsim = sp.getString("sim", "");
			//得到手机现在的sim卡序列号
			TelephonyManager tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String realsim = tm.getSimSerialNumber();
			if(bindsim.equals(realsim)){
				Log.i("", "sim卡未发生变化");
			}else{
				Log.i("", "sim卡发生变化");
				//发送短信，系统版而定
				String safenumber = sp.getString("safephone", "");
				if(!TextUtils.isEmpty(safenumber)){
					SmsManager smsManager=SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null, "您的亲友手机SIM卡已经被变更", null, null);
				}
				
			}
			
		}
	}
}
