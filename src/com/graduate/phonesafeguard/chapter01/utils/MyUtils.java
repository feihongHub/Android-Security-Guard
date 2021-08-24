package com.graduate.phonesafeguard.chapter01.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class MyUtils {
	/**
	 * 获取包的版本号信息
	 */
	public static int getVersion(Context context){
		//通过上下文获取包管理器
		PackageManager manager=context.getPackageManager();
		try {
			//获取当前程序包名
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		
	}
	public static String getVersionName(Context context){
		//通过上下文获取包管理器
		PackageManager manager=context.getPackageManager();
		try {
			//获取当前程序包名
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	/**
	 * 安装Apk
	 * 
	 */
	public static void installApk(Activity activity){
		Intent intent=new Intent("android.intent.action.VIEW");
		//添加默认分类
		intent.addCategory("android.intent.category.DEFAULT");
		//设置数据和类型
		intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/safe2.apk")), "application/vnd.android.package-archive");
		//如果开启的Activity退出时会调用当前Activity的onActivityResult
		activity.startActivityForResult(intent, 0);
	}

}
