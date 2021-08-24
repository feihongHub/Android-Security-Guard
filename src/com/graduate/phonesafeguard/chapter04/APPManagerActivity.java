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
//������
public class APPManagerActivity extends Activity implements OnClickListener{
	/**
	 * �ֻ�����ʣ��TextView
	 */
	private TextView mPhoneMemoryTV;
	/**
	 * ����sd��ʣ��TextView
	 */
	private TextView mSDMemoryTV;
	/**
	 * ��ʾ����SD���ڴ�TextView
	 */
	private TextView mOutSDMemoryTV;
	private ListView mListView;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos=new ArrayList<AppInfo>();
	private List<AppInfo> systemAppInfos=new ArrayList<AppInfo>();
	private AppManagerAdapter adapter;
	private UninstallReceiver receiver;
	/**
	 * ����Ӧ�ó���ж�سɹ��Ĺ㲥
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
		//ע��㲥
		receiver = new UninstallReceiver();
		IntentFilter intentFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addDataScheme("package");
		registerReceiver(receiver, intentFilter);
		initView();
		
	}
	//��ʼ���ؼ�
	private void initView(){
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.tv_yellow));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("�������");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		mPhoneMemoryTV=(TextView) findViewById(R.id.tv_phonememory_appmanager);
		mSDMemoryTV=(TextView) findViewById(R.id.tv_sdmemory_appmanager);
		mOutSDMemoryTV=(TextView) findViewById(R.id.tv_outsdmemory_appmanager);
		mAppNumTV=(TextView) findViewById(R.id.tv_appnumber);
		mListView=(ListView) findViewById(R.id.lv_appmanager);
		//��ȡ�ֻ�ʣ�����ݺ�SD��ʣ��ռ�
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
							//��ס��ǰ��Ŀ
							boolean flag=mappInfo.isSelected;
							//�����������е���Ŀ��appinfo��Ϊδѡ�е�״̬
							for(AppInfo appInfo:userAppInfos){
								appInfo.isSelected=false;
							}
							for (AppInfo appInfo : systemAppInfos) {
								appInfo.isSelected=false;
							}
							if(mappInfo!=null){
								//����Ѿ�ѡ�У����Ϊδѡ��
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
					mAppNumTV.setText("ϵͳ����"+systemAppInfos.size()+"��");
				}else{
					mAppNumTV.setText("�û�����"+userAppInfos.size()+"��");
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
					//������û�App
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
		long availableBlocks=stat.getAvailableBlocks();//��ȡ���ô洢������
		long blockSize=stat.getBlockSize();//ÿ���洢���С
		long blockCount = stat.getBlockCount();
		//���ô洢�ռ�
		long availSize=availableBlocks * blockSize;
		long totalSize=blockSize*blockCount;//����������
		//Integer.MAX_VALUE���Ա�ʾ2G��С��2G̫С����Ҫ��Long
		return Formatter.formatFileSize(this, availSize);//���ֽ�תΪ��Ӧ��λ
	}
	//�õ��ֻ���SD��ʣ���ڴ�
		private void getMemoryFromPhone(){
			String avail_sd = Environment.getExternalStorageDirectory().getAbsolutePath();
			String str_avail_sd = getAvailSpace(avail_sd);
			//����sd��·��
			//�ж��������sd��
			String avail_rom = Environment.getDataDirectory().getAbsolutePath();
			String str_avail_rom = getAvailSpace(avail_rom);
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				String str_sd_out_rom = getAvailSpace("mnt/sdcard2");
				mOutSDMemoryTV.setText("SD��ʣ��:"+str_sd_out_rom);
			}
//			long avail_rom=Environment.getDataDirectory().getFreeSpace();
//			long avail_sd = Environment.getExternalStorageDirectory().getFreeSpace();
//			
//			String str_avail_rom = Formatter.formatFileSize(this, avail_rom);
//			String str_avail_sd = Formatter.formatFileSize(this, avail_sd);
			
			mPhoneMemoryTV.setText("����ʣ��:"+str_avail_rom);
			mSDMemoryTV.setText("�ڲ��洢ʣ��:"+str_avail_sd);
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
	 * ����Ӧ�ó���㲥
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
