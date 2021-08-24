package com.graduate.phonesafeguard.chapter02;

import com.graduate.phonesafeguard.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseSetUpActivity extends Activity {
	public SharedPreferences sp;
	public GestureDetector mGestureDetector;
//	public VelocityTracker mvelocityTracker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				if(Math.abs(velocityX)<200){
					Toast.makeText(getApplicationContext(), "无效动作，移动太慢", 0).show();
					return true;
				}
				if((e2.getRawX()-e1.getRawX())>200){
					//从左向右滑动屏幕,显示上一个页面
					showPre();
					overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
					return true;
				}
				if((e1.getRawX()-e2.getRawX())>200){
					//从右向左滑动解锁，显示下一个界面
					showNext();
					overridePendingTransition(R.anim.next_in, R.anim.next_out);
					return true ;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	//两个抽象方法
	public abstract void showNext();
	public abstract void showPre();
	//用手势识别器去识别事件
	public boolean onTouchEvent(MotionEvent event){
		//分析手势识别器
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	//开启新的Activity并关闭自己
	public void startActivityAndFinishSelf(Class<?> cla){
		Intent intent=new Intent(this,cla);
		startActivity(intent);
		finish();
	}
}
