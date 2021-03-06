package com.graduate.phonesafeguard.chapter07.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class SystemInfoUtils {
	/**
	 * 判断一个服务是否处于运行状态
	 */
	public static boolean isServiceRunning(Context context,String className){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos=am.getRunningServices(200);
		for (RunningServiceInfo info : infos) {
			String serviceClassName=info.service.getClassName();
			if(className.equals(serviceClassName)){
				return true;
			}
		}
		return false;	
	}
	/**
	 * 获取手机的总内存大小单位byte
	 * 
	 */
	public static long getTotalMem(){
		FileInputStream fis;
		try {
			fis = new FileInputStream(new File("/proc/meminfo"));
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String totalInfo = br.readLine();
			StringBuffer sb=new StringBuffer();
			for (char c : totalInfo.toCharArray()) {
				if(c>='0' && c<='9'){
					sb.append(c);
				}
			}
			long bytesize=Long.parseLong(sb.toString())*1024;
			return bytesize;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} 
	
	}
	/**
	 * 获取可用内存信息
	 */
	public static long getAvailMem(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取内存大小
		MemoryInfo outInfo=new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMem = outInfo.availMem;
		return availMem;
		
	}
	/**
	 * 得到正在运行的进程数量
	 */
	public static int getRunningPocessCount(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfos=am.getRunningAppProcesses();
		int count=runningAppProcessInfos.size();
		return count;
	}
}
