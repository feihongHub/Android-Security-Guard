package com.graduate.phonesafeguard.chapter06;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import com.graduate.phonesafeguard.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CleanCacheActivity extends Activity implements OnClickListener{
	protected static final int CLEANNING=100;
	private RotateAnimation anim;
	private TextView mMemoryTV;
	private TextView mMemoryUnitTV;
	private FrameLayout mCleanCacheFL;
	private FrameLayout mFinishCleanFL;
	private TextView mSizeTV;
	private PackageManager pm;
	private long cacheMemory;
	private ImageView ivScanning;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CLEANNING:
				long memory=(Long)msg.obj;
				formaMemory(memory);
				if(memory==cacheMemory){
					//清除动画
					ivScanning.clearAnimation();
					mCleanCacheFL.setVisibility(View.GONE);
					mFinishCleanFL.setVisibility(View.VISIBLE);
					mSizeTV.setText("成功清除:"+Formatter.formatFileSize(CleanCacheActivity.this, cacheMemory));
				}
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cleancache);
		initView();
		pm = getPackageManager();
		Intent intent=getIntent();
		cacheMemory = intent.getLongExtra("cacheMemory", 0);
		initData();
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
		((TextView)findViewById(R.id.tv_title)).setText("缓存清理");
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		ivScanning = (ImageView) findViewById(R.id.act_scanning);
		anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(1500);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		ivScanning.startAnimation(anim);
		mMemoryTV = (TextView) findViewById(R.id.tv_cleancache_memory);
		mMemoryUnitTV = (TextView) findViewById(R.id.tv_cleancache_memoryunit);
		mCleanCacheFL = (FrameLayout) findViewById(R.id.fl_cleancache);
		mFinishCleanFL = (FrameLayout) findViewById(R.id.fl_finishclean);
		mSizeTV = (TextView) findViewById(R.id.tv_cleanmemorysize);
		findViewById(R.id.btn_finish_cleancache).setOnClickListener(this);
	}
	private void initData(){
		cleanAll();
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				long memory=0;
				while(memory<cacheMemory){
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Random rand=new Random();
					int i = rand.nextInt();
					i=rand.nextInt(1024);
					memory+=1024*i;
					if(memory>cacheMemory){
						memory=cacheMemory;
					}
					Message message=Message.obtain();
					message.what=CLEANNING;
					message.obj=memory;
					handler.sendMessageDelayed(message, 200);
				}
			};
		}.start();
	}
	/**
	 * 初始化数据
	 */
	private void formaMemory(long memory){
		String cacheMemoryStr=Formatter.formatFileSize(this, memory);
		String memoryStr;
		String memoryUnit;
		//根据大小判定单位
		if(memory>900){
			//大于900则单位是两位
			memoryStr = cacheMemoryStr.substring(0, cacheMemoryStr.length()-2);
			memoryUnit=cacheMemoryStr.substring(cacheMemoryStr.length()-2, cacheMemoryStr.length());
			
		}else{
			//单位是一位
			memoryStr=cacheMemoryStr.substring(0, cacheMemoryStr.length()-1);
			memoryUnit=cacheMemoryStr.substring(cacheMemoryStr.length()-1, cacheMemoryStr.length());
		}
		mMemoryTV.setText(memoryStr);
		mMemoryUnitTV.setText(memoryUnit);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.btn_finish_cleancache:
			finish();
			break;

		}
	}
	class ClearCacheObserver extends Stub{

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		
	}
	private void cleanAll(){//在Android高版本的手机中，有些系统APP 会预留一些缓存空间，12kb，死活清理不了
		//清除全部缓存利用Android系统的一个漏洞：freeStorageAndNotify，向系统索要足够大的空间来释放所有缓存
		try {
			Method method = pm.getClass().getMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
			method.invoke(pm,Long.MAX_VALUE, new IPackageDataObserver.Stub() {			
				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
					// TODO Auto-generated method stub
					System.out.println("delete success"+succeeded);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Method[] methods = PackageManager.class.getMethods();
//		for (Method method : methods) {
//			if("freeStorageAndNotify".equals(method.getName())){
//				try {
//					method.invoke(pm, Long.MAX_VALUE,new ClearCacheObserver());
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return;
//			}
//		}
		Toast.makeText(this, "清理中...", 0).show();
	}
}
