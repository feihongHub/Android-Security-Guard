package com.graduate.phonesafeguard.chapter04.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.chapter04.entity.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfoParser {
	private static AppInfo appinfo;

	/**
	 * 获取手机上所有应用
	 */
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm=context.getPackageManager();
		//此方法可以将系统中手动或系统安装的所有安装信息
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appinfos=new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			//实例化对象
			appinfo = new AppInfo();
			//获取包名
			String packname=packInfo.packageName;
			appinfo.packageName=packname;
			//获取应用图标
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			appinfo.icon=icon;
			//获取应用名称
			String appname = packInfo.applicationInfo.loadLabel(pm).toString();
			appinfo.appName=appname;
			//应用程序apk安装路径
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.apkPath=apkpath;
			//建立一个文件对象,用来获取应用大小
			File file=new File(apkpath);
			long appsize=file.length();
			appinfo.appSize=appsize;
			//应用程序安装位置
			int flags = packInfo.applicationInfo.flags;//二进制映射
			if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){
				//外部存储
				appinfo.isInRoom=false;
			}else{
				//手机内存
				appinfo.isInRoom=true;
			}
			if((ApplicationInfo.FLAG_SYSTEM & flags)!=0){
				//系统应用
				appinfo.isUserApp=false;
			}else{
				//用户应用
				appinfo.isUserApp=true;
			}
			appinfos.add(appinfo);
			appinfo=null;
		}
		return appinfos;
		
	}
	
}
