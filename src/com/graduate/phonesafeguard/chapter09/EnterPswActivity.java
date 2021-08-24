package com.graduate.phonesafeguard.chapter09;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter02.utils.MD5Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class EnterPswActivity extends Activity implements OnClickListener {
	private SharedPreferences mSp;
	private String password;
	private String packagename;
	private ImageView mAppIcon;
	private TextView mAppNameTV;
	private EditText mPswET;
	private Button mGoImgv;
	private LinearLayout mEnterPswLL;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_enterpsw);
		mSp = getSharedPreferences("config", MODE_PRIVATE);
		password = mSp.getString("PhoneAntiTheftPWD", null);
		Intent intent=getIntent();
		System.out.println("caonima");
		packagename = intent.getStringExtra("packagename");
		PackageManager pm=getPackageManager();
		initView();
		try {
			mAppIcon.setImageDrawable(pm.getApplicationInfo(packagename, 0).loadIcon(pm));
			mAppNameTV.setText(pm.getApplicationInfo(packagename, 0).loadLabel(pm).toString());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initView() {
		mAppIcon = (ImageView) findViewById(R.id.imgv_appicon_enterpsw);
		mAppNameTV = (TextView) findViewById(R.id.tv_appname_enterpsw);
		mPswET = (EditText) findViewById(R.id.et_psw_enterpsw);
		mGoImgv = (Button) findViewById(R.id.imgv_go_enterpsw);
		mEnterPswLL = (LinearLayout) findViewById(R.id.ll_enterpsw);
		mGoImgv.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_go_enterpsw:
			//比较密码
			String inputpsw = mPswET.getText().toString().trim();
			if(TextUtils.isEmpty(inputpsw)){
				startAnim();
				Toast.makeText(this, "请输入密码", 0).show();
				return;
			}else{
				if(!TextUtils.isEmpty(password)){
					if(MD5Utils.encode(inputpsw).equals(password)){
						//发送自定义广播消息
						Intent intent=new Intent();
						intent.setAction("com.graduate.phonesafeguard.applock");
						intent.putExtra("packagename", packagename);
						sendBroadcast(intent);
						finish();
					}else{
						startAnim();
						Toast.makeText(this, "密码不正确", 0).show();
						return;
					}
				}
			}
			break;
		}
	}
	private void startAnim(){
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.shake);
		mEnterPswLL.startAnimation(animation);
	}
	//拦截物理返回键
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		finish();
	}

}
