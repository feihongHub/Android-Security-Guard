package com.graduate.phonesafeguard.chapter07.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoKillProcessService extends Service {

	private ScreenLockReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		receiver = new ScreenLockReceiver();
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		receiver=null;
		super.onDestroy();
	}
	class ScreenLockReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ActivityManager am=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for (RunningAppProcessInfo info : am.getRunningAppProcesses()) {
				String packname = info.processName;
				if(packname.equals(context.getPackageName())){
					System.out.println("不能自己干自己啊");
					continue;
				}
				am.killBackgroundProcesses(packname);
				System.out.println("已经干掉很多进程了");
			}
		}
		
	}
	

}
