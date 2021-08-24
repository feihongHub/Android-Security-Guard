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
	 * ��ȡ���İ汾����Ϣ
	 */
	public static int getVersion(Context context){
		//ͨ�������Ļ�ȡ��������
		PackageManager manager=context.getPackageManager();
		try {
			//��ȡ��ǰ�������
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		
	}
	public static String getVersionName(Context context){
		//ͨ�������Ļ�ȡ��������
		PackageManager manager=context.getPackageManager();
		try {
			//��ȡ��ǰ�������
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	/**
	 * ��װApk
	 * 
	 */
	public static void installApk(Activity activity){
		Intent intent=new Intent("android.intent.action.VIEW");
		//���Ĭ�Ϸ���
		intent.addCategory("android.intent.category.DEFAULT");
		//�������ݺ�����
		intent.setDataAndType(Uri.fromFile(new File("/mnt/sdcard/safe2.apk")), "application/vnd.android.package-archive");
		//���������Activity�˳�ʱ����õ�ǰActivity��onActivityResult
		activity.startActivityForResult(intent, 0);
	}

}
