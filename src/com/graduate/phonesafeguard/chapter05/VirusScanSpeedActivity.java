package com.graduate.phonesafeguard.chapter05;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter05.adapter.ScanVirusAdapter;
import com.graduate.phonesafeguard.chapter05.dao.AntiVirusDao;
import com.graduate.phonesafeguard.chapter05.entity.ScanAppInfo;
import com.graduate.phonesafeguard.chapter05.utils.MD5Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VirusScanSpeedActivity extends Activity implements OnClickListener{
	protected static final int SCAN_BENGIN=100;
	protected static final int SCANNIMG=101;
	protected static final int SCAN_FINISH=102;
	private int total;
	private PackageManager pm;
	private SharedPreferences mSP;
	private TextView mProcessTV;
	private TextView mScanAppTV;
	private Button mCancleBtn;
	private ListView mScanListView;
	private ScanVirusAdapter adapter;
	private List<ScanAppInfo> mScanAppInfos=new ArrayList<ScanAppInfo>();
	private ImageView mScanningIcon;
	private RotateAnimation rani;
	private boolean flag;
	private boolean isStop;
	private int process;
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_BENGIN:
				mScanAppTV.setText("初始化杀毒引擎中...");
				break;
			case SCANNIMG:
				ScanAppInfo info=(ScanAppInfo) msg.obj;
				mScanAppTV.setText("正在扫描:"+info.appName);
				int speed=msg.arg1;
				mProcessTV.setText((speed*100/total)+"%");
				mScanAppInfos.add(info);
				adapter.notifyDataSetChanged();
				mScanListView.setSelection(mScanAppInfos.size());
				break;
			case SCAN_FINISH:
				mScanAppTV.setText("扫描完成！");
				mScanningIcon.clearAnimation();
				mCancleBtn.setBackgroundResource(R.drawable.cancle_scan_p);
				mCancleBtn.setText("完成");
				saveScanTime();
				break;
			}
		};
	};
	private void saveScanTime(){
		Editor edit=mSP.edit();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		String currentTime=sdf.format(new Date());
		currentTime="上次查杀:"+currentTime;
		edit.putString("lastVirusScan", currentTime);
		edit.commit();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_virusscanspeed);
		pm = getPackageManager();
		mSP = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		scanVirus();
	}
	private void scanVirus(){
		flag=true;
		isStop=false;
		process=0;
		mScanAppInfos.clear();
		new Thread(){
			public void run() {
				Message msg=Message.obtain();
				msg.what=SCAN_BENGIN;
				mHandler.sendMessage(msg);
				List<PackageInfo> installedPackages=pm.getInstalledPackages(0);
				total=installedPackages.size();
				for (PackageInfo info : installedPackages) {
					if(!flag){
						isStop=true;
						return;
					}
					String apkpath=info.applicationInfo.sourceDir;
					//检查获取这个文件的特征码
					String md5info = MD5Utils.getFileMd5(apkpath);
					//返回结果
					String result = AntiVirusDao.checkVirus(md5info);
					msg=Message.obtain();
					msg.what=SCANNIMG;
					//发消息
//					mHandler.sendMessage(msg);
					ScanAppInfo scanAppInfo=new ScanAppInfo();
					if(result==null){
						scanAppInfo.description="扫描完成";
						scanAppInfo.isVirus=false;
					}else{
						scanAppInfo.description=result;
						scanAppInfo.isVirus=true;
					}
					process++;
					scanAppInfo.packagename=info.packageName;
					scanAppInfo.appName=info.applicationInfo.loadLabel(pm).toString();
					scanAppInfo.appicon=info.applicationInfo.loadIcon(pm);
					//消息携带对象
//					msg=Message.obtain();
					msg.obj=scanAppInfo;
					msg.arg1=process;
					mHandler.sendMessage(msg);
					try {
						//杀毒扫描每个app之间的间隔
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				msg=Message.obtain();
				msg.what=SCAN_FINISH;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_blue));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("病毒查杀进度");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.title_back);
		mProcessTV = (TextView) findViewById(R.id.tv_scanprocess);
		mScanAppTV = (TextView) findViewById(R.id.tv_scansapp);
		mCancleBtn = (Button) findViewById(R.id.btn_canclescan);
		mCancleBtn.setOnClickListener(this);
		mScanListView = (ListView) findViewById(R.id.lv_scanapps);
		adapter=new ScanVirusAdapter(mScanAppInfos, this);
		mScanListView.setAdapter(adapter);
		mScanningIcon = (ImageView) findViewById(R.id.imgv_scanningicon);
		startAnim();
	}
	private void startAnim(){
		if(rani==null){
			rani=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		}
		rani.setRepeatCount(Animation.INFINITE);
		rani.setDuration(2000);
		mScanningIcon.startAnimation(rani);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.btn_canclescan:
			if(process==total & process>0){
				//扫描已完成
				finish();
			}else if(process>0 & process<total & isStop==false){
				mScanningIcon.clearAnimation();
				//取消扫描
				flag=false;
				//更换背景图片
				mCancleBtn.setBackgroundResource(R.drawable.cancle_scan_btn_selector);
				mCancleBtn.setText("重新扫描");

			}else if(isStop){
				startAnim();
				//重新扫描
				scanVirus();
				//更换背景图片
				mCancleBtn.setBackgroundResource(R.drawable.cancle_scan_btn_selector);
				mCancleBtn.setText("停止扫描");
			}
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		flag=false;
		super.onDestroy();
	}
}
