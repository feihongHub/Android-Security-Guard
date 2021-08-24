package com.graduate.phonesafeguard.chapter02.receiver;

import com.graduate.phonesafeguard.chapter02.App;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	private static final String TAG=BootCompleteReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//≥ı ºªØ
		((App)context.getApplicationContext()).correctSIM();
	}

}
