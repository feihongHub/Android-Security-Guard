package com.graduate.phonesafeguard.chapter07.utils;

import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter07.entity.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class TaskInfoParser {
	/**
	 * 获取正在运行的所有进程信息
	 */
	public static List<TaskInfo> getRunningTaskInfos(Context context){
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm=context.getPackageManager();
		List<RunningAppProcessInfo> processInfos=am.getRunningAppProcesses();
		List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo processInfo : processInfos) {
			String packname=processInfo.processName;
			TaskInfo taskInfo=new TaskInfo();
			taskInfo.packageName=packname;
			android.os.Debug.MemoryInfo[] memoryInfos=am.getProcessMemoryInfo(new int[]{processInfo.pid});
			long memsize=memoryInfos[0].getTotalPrivateDirty()*1024;
			taskInfo.appMemory=memsize;//程序占用空间
			try {
				PackageInfo packInfo=pm.getPackageInfo(packname, 0);
				Drawable icon=packInfo.applicationInfo.loadIcon(pm);
				taskInfo.appIcon=icon;
				String name=packInfo.applicationInfo.loadLabel(pm).toString();
				taskInfo.appName=name;
				if((ApplicationInfo.FLAG_SYSTEM & packInfo.applicationInfo.flags)!=0){
					//系统进程
					taskInfo.isUserApp=false;
				}else{
					//用户进程
					taskInfo.isUserApp=true;
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskInfo.appName=packname;
				taskInfo.appIcon=context.getResources().getDrawable(R.drawable.ic_launcher);
			}
			taskInfos.add(taskInfo);
			
		}
		return taskInfos;
		
	}
}
