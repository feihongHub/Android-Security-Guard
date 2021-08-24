package com.graduate.phonesafeguard.chapter09.entity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
			appinfos.add(appinfo);
			appinfo=null;
		}
		return appinfos;
		
	}
}
