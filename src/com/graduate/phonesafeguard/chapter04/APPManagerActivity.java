package com.graduate.phonesafeguard.chapter04;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter04.adapter.AppManagerAdapter;
import com.graduate.phonesafeguard.chapter04.entity.AppInfo;
import com.graduate.phonesafeguard.chapter04.utils.AppInfoParser;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
//有问题
public class APPManagerActivity extends Activity implements OnClickListener{
	/**
	 * 手机自身剩余TextView
	 */
	private TextView mPhoneMemoryTV;
	/**
	 * 内置sd卡剩余TextView
	 */
	private TextView mSDMemoryTV;
	/**
	 * 显示外置SD卡内存TextView
	 */
	private TextView mOutSDMemoryTV;
	private ListView mListView;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos=new ArrayList<AppInfo>();
	private List<AppInfo> systemAppInfos=new ArrayList<AppInfo>();
	private AppManagerAdapter adapter;
	private UninstallReceiver receiver;
	/**
	 * 接受应用程序卸载成功的广播
	 * @param v
	 */
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				if(adapter==null){
					adapter=new AppManagerAdapter(userAppInfos, systemAppInfos, APPManagerActivity.this);
					
				}
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case 15:
				adapter.notifyDataSetChanged();
				break;
			}
		};
	};
	private TextView mAppNumTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_manager);
		//注册广播
		receiver = new UninstallReceiver();
		IntentFilter intentFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addDataScheme("package");
		registerReceiver(receiver, intentFilter);
		initView();
		
	}
	//初始化控件
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.tv_yellow));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("软件管理");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		mPhoneMemoryTV=(TextView) findViewById(R.id.tv_phonememory_appmanager);
		mSDMemoryTV=(TextView) findViewById(R.id.tv_sdmemory_appmanager);
		mOutSDMemoryTV=(TextView) findViewById(R.id.tv_outsdmemory_appmanager);
		mAppNumTV=(TextView) findViewById(R.id.tv_appnumber);
		mListView=(ListView) findViewById(R.id.lv_appmanager);
		//获取手机剩余内容和SD卡剩余空间
		getMemoryFromPhone();
		initDate();
		initListener();
	}
	private void initListener(){
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				if(adapter!=null){
					new Thread(){
						public void run() {
							AppInfo mappInfo=(AppInfo) adapter.getItem(position);
							//记住当前条目
							boolean flag=mappInfo.isSelected;
							//将集合中所有的条目的appinfo变为未选中的状态
							for(AppInfo appInfo:userAppInfos){
								appInfo.isSelected=false;
							}
							for (AppInfo appInfo : systemAppInfos) {
								appInfo.isSelected=false;
							}
							if(mappInfo!=null){
								//如果已经选中，则变为未选中
								if(flag){
									mappInfo.isSelected=false;
								}else {
									mappInfo.isSelected=true;
								}
								mHandler.sendEmptyMessage(15);
							}
						};
					}.start();
				}
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem>=userAppInfos.size()+1){
					mAppNumTV.setText("系统程序"+systemAppInfos.size()+"个");
				}else{
					mAppNumTV.setText("用户程序"+userAppInfos.size()+"个");
				}
			}
		});
	}
	private void initDate(){
		appInfos=new ArrayList<AppInfo>();
		new Thread(){
			public void run() {
				appInfos.clear();
				userAppInfos.clear();
				systemAppInfos.clear();
				appInfos.addAll(AppInfoParser.getAppInfos(APPManagerActivity.this));
				for (AppInfo appInfo : appInfos) {
					//如果是用户App
					if(appInfo.isUserApp){
						userAppInfos.add(appInfo);
					}else{
						systemAppInfos.add(appInfo);
					}
				}
				mHandler.sendEmptyMessage(10);
			};
		}.start();
	}
	private String getAvailSpace(String path){
		StatFs stat=new StatFs(path);
		long availableBlocks=stat.getAvailableBlocks();//获取可用存储块数量
		long blockSize=stat.getBlockSize();//每个存储块大小
		long blockCount = stat.getBlockCount();
		//可用存储空间
		long availSize=availableBlocks * blockSize;
		long totalSize=blockSize*blockCount;//计算总容量
		//Integer.MAX_VALUE可以表示2G大小，2G太小，需要用Long
		return Formatter.formatFileSize(this, availSize);//将字节转为相应单位
	}
	//拿到手机和SD卡剩余内存
		private void getMemoryFromPhone(){
			String avail_sd = Environment.getExternalStorageDirectory().getAbsolutePath();
			String str_avail_sd = getAvailSpace(avail_sd);
			//外置sd卡路径
			//判断有误外界sd卡
			String avail_rom = Environment.getDataDirectory().getAbsolutePath();
			String str_avail_rom = getAvailSpace(avail_rom);
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				String str_sd_out_rom = getAvailSpace("mnt/sdcard2");
				mOutSDMemoryTV.setText("SD卡剩余:"+str_sd_out_rom);
			}
//			long avail_rom=Environment.getDataDirectory().getFreeSpace();
//			long avail_sd = Environment.getExternalStorageDirectory().getFreeSpace();
//			
//			String str_avail_rom = Formatter.formatFileSize(this, avail_rom);
//			String str_avail_sd = Formatter.formatFileSize(this, avail_sd);
			
			mPhoneMemoryTV.setText("机身剩余:"+str_avail_rom);
			mSDMemoryTV.setText("内部存储剩余:"+str_avail_sd);
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.imgv_leftbtn:
				finish();
				break;
			}
		}
	/**
	 * 接受应用程序广播
	 * @param v
	 */
	class UninstallReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			initDate();
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		receiver=null;
		super.onDestroy();
	}
}
