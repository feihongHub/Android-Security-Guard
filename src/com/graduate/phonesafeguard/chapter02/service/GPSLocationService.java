package com.graduate.phonesafeguard.chapter02.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

public class GPSLocationService extends Service {
	private LocationManager lm;
	private MyListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		
		//查询条件
		//true只返回可用的位置提供者
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取精准信息
		criteria.setCostAllowed(true);//允许产生花销
		String name = lm.getBestProvider(criteria, true);
		System.out.println("最好的位置提供者:"+name);
		listener=new MyListener();
		lm.requestLocationUpdates(name, 0, 0, listener);
	}
	private class MyListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			StringBuilder sb=new StringBuilder();
			sb.append("accuracy:"+location.getAccuracy()+"\n");
			sb.append("speed:"+location.getSpeed()+"\n");
			sb.append("jingdu:"+location.getLongitude()+"\n");
			sb.append("weidu:"+location.getLatitude()+"\n");
			
			String result = sb.toString();
			System.out.println("位置"+result);
			SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
			String safenumber = sp.getString("safephone", "");
//			System.out.println("安全号码"+safenumber);
			SmsManager smsManager=SmsManager.getDefault();
			smsManager.sendTextMessage(safenumber, null, result, null, null);
			System.out.println("安全号码"+safenumber);
			stopSelf();//服务自杀
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(listener);
		listener=null;
	}

}
