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
	 * ��ȡ�ֻ�������Ӧ��
	 */
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm=context.getPackageManager();
		//�˷������Խ�ϵͳ���ֶ���ϵͳ��װ�����а�װ��Ϣ
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appinfos=new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			//ʵ��������
			appinfo = new AppInfo();
			//��ȡ����
			String packname=packInfo.packageName;
			appinfo.packageName=packname;
			//��ȡӦ��ͼ��
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			appinfo.icon=icon;
			//��ȡӦ������
			String appname = packInfo.applicationInfo.loadLabel(pm).toString();
			appinfo.appName=appname;
			//Ӧ�ó���apk��װ·��
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.apkPath=apkpath;
			//����һ���ļ�����,������ȡӦ�ô�С
			File file=new File(apkpath);
			long appsize=file.length();
			appinfo.appSize=appsize;
			//Ӧ�ó���װλ��
			int flags = packInfo.applicationInfo.flags;//������ӳ��
			if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){
				//�ⲿ�洢
				appinfo.isInRoom=false;
			}else{
				//�ֻ��ڴ�
				appinfo.isInRoom=true;
			}
			if((ApplicationInfo.FLAG_SYSTEM & flags)!=0){
				//ϵͳӦ��
				appinfo.isUserApp=false;
			}else{
				//�û�Ӧ��
				appinfo.isUserApp=true;
			}
			appinfos.add(appinfo);
			appinfo=null;
		}
		return appinfos;
		
	}
	
}
