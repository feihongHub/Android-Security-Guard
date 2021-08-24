package com.graduate.phonesafeguard.chapter02.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
//定义特殊的广播接收者，系统超级管理员的广播接收者
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}
}
