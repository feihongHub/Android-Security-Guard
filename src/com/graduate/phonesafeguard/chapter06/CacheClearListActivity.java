package com.graduate.phonesafeguard.chapter06;

import java.lang.reflect.InvocationTargetException; 
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter06.adapter.CacheCleanAdapter;
import com.graduate.phonesafeguard.chapter06.entity.CacheInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CacheClearListActivity extends Activity implements OnClickListener{
	protected static final int SCANNING=100;
	protected static final int FINISH=101;
	private AnimationDrawable animation;
	private PackageManager pm;
	private TextView mRecomandTV;
	private TextView mCanCleanTV;
	private ListView mCacheLV;
	private List<CacheInfo> mCacheInfos=new ArrayList<CacheInfo>();
	private List<CacheInfo> cacheInfos=new ArrayList<CacheInfo>();
	private CacheCleanAdapter adapter;
	private Button mCacheBtn;
	private long cacheMemory;
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANNING:
				PackageInfo info=(PackageInfo) msg.obj;
				mRecomandTV.setText("正在扫描:"+info.packageName);
				mCanCleanTV.setText("已扫描缓存:"+Formatter.formatFileSize(CacheClearListActivity.this, cacheMemory));
				//在主线程添加变化后集合
				mCacheInfos.clear();
				mCacheInfos.addAll(cacheInfos);
				//ListView刷新
				adapter.notifyDataSetChanged();
				mCacheLV.setSelection(mCacheInfos.size());
				mCacheBtn.setText("正在检测...");
				break;
			case FINISH:
				animation.stop();
				if(cacheMemory>0){
					mCacheBtn.setEnabled(true);
					mCacheBtn.setText("一键清理");
				}else{
					mCacheBtn.setEnabled(true);
					mCacheBtn.setText("点击返回");
					Toast.makeText(CacheClearListActivity.this, "您的手机很干净", 0).show();
				}
				break;
			}
		};
	};
	
	
	private Thread thread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cacheclearlist);
		pm = getPackageManager();
		initView();
	}
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_rose_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		((TextView)findViewById(R.id.tv_title)).setText("缓存扫描");
		mRecomandTV = (TextView) findViewById(R.id.tv_recommend_clean);
		mCanCleanTV = (TextView) findViewById(R.id.tv_can_clean);
		mCacheLV = (ListView) findViewById(R.id.lv_scancache);
		mCacheBtn = (Button) findViewById(R.id.btn_cleanall);
		mCacheBtn.setOnClickListener(this);
		animation = (AnimationDrawable)findViewById(R.id.imgv_broom).getBackground();
		animation.setOneShot(false);
		animation.start();
		adapter=new CacheCleanAdapter(this, mCacheInfos);
		mCacheLV.setAdapter(adapter);
		fillData();
	}
	/**
	 * 填充数据
	 */
	private void fillData(){
		thread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				cacheInfos.clear();
				List<PackageInfo> infos=pm.getInstalledPackages(0);
				for (PackageInfo info : infos) {
					getCacheSize(info);
					try {
						//
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg=Message.obtain();
					msg.obj=info;
					msg.what=SCANNING;
					mHandler.sendMessage(msg);
				}
				Message msg=Message.obtain();
				msg.what=FINISH;
				mHandler.sendMessage(msg);
			};
		};
		thread.start();
	}
	/**
	 * 获取某个包的对应的缓存大小
	 */
	public void getCacheSize(PackageInfo info){
		//键值对
		try {
			Method method=PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			method.invoke(pm, info.packageName,new MyPackObserver(info));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		animation.stop();
		if(thread!=null){
			thread.interrupt();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.btn_cleanall:
			if(cacheMemory>0){
				Intent intent=new Intent(this,CleanCacheActivity.class);
				intent.putExtra("cacheMemory", cacheMemory);
				startActivity(intent);
				finish();
			}else{
				finish();
			}
			break;
		}
	}
	private class MyPackObserver extends Stub{
		private PackageInfo info;
		public MyPackObserver(PackageInfo info){
			this.info=info;
		}
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
			// TODO Auto-generated method stub
			long cacheSize = pStats.cacheSize;
			if(cacheSize>=0){
				CacheInfo cacheInfo=new CacheInfo();
				cacheInfo.cacheSize=cacheSize;
				cacheInfo.packagename=info.packageName;
				cacheInfo.appName=info.applicationInfo.loadLabel(pm).toString();
				cacheInfo.appIcon=info.applicationInfo.loadIcon(pm);
				cacheInfos.add(cacheInfo);
				cacheMemory+=cacheSize;
			}
		}
	}
}
