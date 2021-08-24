package com.graduate.phonesafeguard.chapter10.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 判断服务是否运行
 * @author Administrator
 *
 */
public class ServiceStatusUtils {

	public static boolean isServiceRunning(String serviceName,Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			if(className.equals(serviceName)){
				return true;
			}
			
		}
		return false;
	}
}
