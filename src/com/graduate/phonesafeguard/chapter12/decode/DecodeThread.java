package com.graduate.phonesafeguard.chapter12.decode;

import java.util.concurrent.CountDownLatch;

import com.zbar.lib.CaptureActivity;

import android.os.Handler;
import android.os.Looper;

final class DecodeThread extends Thread{
	
	CaptureActivity activity;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(CaptureActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}
	
	//run�����и�һ����ѯ��
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Looper.loop();
	
	}

}
