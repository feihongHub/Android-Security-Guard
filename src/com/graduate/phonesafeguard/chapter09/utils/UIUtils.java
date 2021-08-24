package com.graduate.phonesafeguard.chapter09.utils;

import android.app.Activity;
import android.widget.Toast;

public class UIUtils {
	//·â×°Toast
	public static void showToast(final Activity context,final String msg){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(context, msg, 1).show();
		}else{
			context.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(context, msg, 1).show();
				}
			});
		}
	}
}
