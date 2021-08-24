package com.graduate.phonesafeguard.chapter12.decode;

import android.app.Activity;
import android.content.DialogInterface;

public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {
	private final Activity activityToFinish;
	
	public FinishListener(Activity activityToFinish) {
		this.activityToFinish = activityToFinish;
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		run();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		run();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		activityToFinish.finish();
	}

}
