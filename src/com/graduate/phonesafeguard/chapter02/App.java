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
		//���SIM������
		correctSIM();
	}
	public void correctSIM(){
		SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
		//��ȡ��������״̬
		boolean protecting = sp.getBoolean("protecting", true);
		if(protecting){
			//�õ��󶨵�SIM�����к�
			String bindsim = sp.getString("sim", "");
			//�õ��ֻ����ڵ�sim�����к�
			TelephonyManager tm=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String realsim = tm.getSimSerialNumber();
			if(bindsim.equals(realsim)){
				Log.i("", "sim��δ�����仯");
			}else{
				Log.i("", "sim�������仯");
				//���Ͷ��ţ�ϵͳ�����
				String safenumber = sp.getString("safephone", "");
				if(!TextUtils.isEmpty(safenumber)){
					SmsManager smsManager=SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null, "���������ֻ�SIM���Ѿ������", null, null);
				}
				
			}
			
		}
	}
}
