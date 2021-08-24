package com.graduate.phonesafeguard.chapter01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.R.id;
import com.graduate.phonesafeguard.R.layout;
import com.graduate.phonesafeguard.chapter01.utils.MyUtils;
import com.graduate.phonesafeguard.chapter01.utils.VersionUpdateUtils;
import com.graduate.phonesafeguard.chapter09.db.dao.NumBelongtoDao;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private int mVersion;
	private String mVersionName;
	private SharedPreferences mSp,mSp2;
	private String dbName="address.db";
	private String dbsearchName="commonnum.db";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//更改窗口特性
		setContentView(R.layout.activity_splash);
		mVersion = MyUtils.getVersion(getApplicationContext());
		mVersionName=MyUtils.getVersionName(getApplicationContext());
		mSp=getSharedPreferences("config", MODE_PRIVATE);
		mSp2=getSharedPreferences("home_enter", MODE_PRIVATE);
		initView();
		copyDB(dbName);
		copyDB(dbsearchName);
		final VersionUpdateUtils updateUtils=new VersionUpdateUtils(mVersion, SplashActivity.this,mSp2);
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				if(mSp.getBoolean("auto_update", true)){
					updateUtils.getCloudVersion();//当获取当前包名后就发出请求
				}else{
					updateUtils.enterHome();
				}	
			}
		}.start();
	}
	
	private void initView(){
		TextView mVersionTV=(TextView) findViewById(R.id.tv_splash_version);
		mVersionTV.setText("版本号："+mVersionName);
	}
	/**
	 * 复制资源目录下的数据库文件
	 */
	public void copyDB(final String dbname){
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				File file=new File(getFilesDir(),dbname);
				if(file.exists()&& file.length()>0){
					System.out.println("数据库已经存在");
					return;
				}
				try {
					InputStream is=getAssets().open(dbname);
					FileOutputStream fos=openFileOutput(dbname, MODE_PRIVATE);
					byte[] buffer=new byte[1024];
					int len=0;
					while ((len=is.read(buffer))!=-1) {
						fos.write(buffer,0,len);
					}
					is.close();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

}
