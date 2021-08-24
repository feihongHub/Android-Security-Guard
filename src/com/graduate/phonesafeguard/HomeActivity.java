package com.graduate.phonesafeguard;

import java.util.ArrayList;  
import java.util.List;
import com.graduate.phonesafeguard.R; 
import com.graduate.phonesafeguard.R.id;
import com.graduate.phonesafeguard.R.layout;
import com.graduate.phonesafeguard.adapter.MainAdapter;
import com.graduate.phonesafeguard.chapter01.adapter.HomeAdapter;
import com.graduate.phonesafeguard.chapter01.adapter.HomeAdapter2;
import com.graduate.phonesafeguard.chapter02.LostFindActivity;
import com.graduate.phonesafeguard.chapter02.dialog.InputPasswordDialog;
import com.graduate.phonesafeguard.chapter02.dialog.SetUpPasswordDialog;
import com.graduate.phonesafeguard.chapter02.dialog.SetUpPasswordDialog.MyCallBack;
import com.graduate.phonesafeguard.chapter02.receiver.MyDeviceAdminReceiver;
import com.graduate.phonesafeguard.chapter02.utils.MD5Utils;
import com.graduate.phonesafeguard.chapter03.SecurityPhoneActivity;
import com.graduate.phonesafeguard.chapter04.APPManagerActivity;
import com.graduate.phonesafeguard.chapter05.VirusScanActivity;
import com.graduate.phonesafeguard.chapter06.CacheClearListActivity;
import com.graduate.phonesafeguard.chapter07.ProcessManagerActivity;
import com.graduate.phonesafeguard.chapter08.TrafficMonitoringActivity;
//import com.graduate.phonesafeguard.chapter08.OperatorSetActivity;
import com.graduate.phonesafeguard.chapter09.AdvanceToolsActivity;
import com.graduate.phonesafeguard.chapter10.SettingsActivity;
import com.graduate.phonesafeguard.chapter11.BatteryObserveActivity;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.zbar.lib.CaptureActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity{
	private GridView gridView,gridView2;
	private SharedPreferences msharedPreferences;//�洢����
	private long mExitTime;
	private DevicePolicyManager policyManager;
	private ViewPager viewPager;
	private ImageView act_main_pagenum;
	private View view1,view2;
	private List<View> listvp;
	private MainAdapter mainAdapter;
	private StartAppAd startAppad=new StartAppAd(this);
	//PhoneAntiTheftPWD�����������
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//���Ĵ�������
		//��ʼ��startAppSDK
		StartAppSDK.init(this, "208024381", true);//AppID,����Ҫ�û�ID
		//��ֹ����ҳ����
		StartAppAd.disableSplash();
		setContentView(R.layout.activity_home);
		
		msharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
		//����ҳ��
		
		viewPager=(ViewPager) findViewById(R.id.act_main_viewpager);
		act_main_pagenum=(ImageView) findViewById(R.id.act_main_pagenum);
		view1 = LayoutInflater.from(this).inflate(R.layout.item_main,
				null);
		view2 = LayoutInflater.from(this).inflate(
				R.layout.item_main2, null);
		
		//gridView������
		gridView=(GridView) view1.findViewById(R.id.gv_home);
		gridView2=(GridView) view2.findViewById(R.id.gv_home2);
		gridView.setAdapter(new HomeAdapter(HomeActivity.this));
		//����ȡ��
		gridView2.setAdapter(new HomeAdapter2(HomeActivity.this));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					//�����ֻ�����ҳ��
					if(isSetUpPassword()){
						showSetupPswdDialog();
					}else{
						showInterPswdDialog();
					}
					break;
				case 1:
					startActivity(SecurityPhoneActivity.class);
					break;
				case 2:
					startActivity(APPManagerActivity.class);
					break;
				case 3:
					startActivity(VirusScanActivity.class);
					break;
				case 4:
					startActivity(CacheClearListActivity.class);
					break;
				case 5:
					startActivity(ProcessManagerActivity.class);
					break;
				case 6:
					startActivity(TrafficMonitoringActivity.class);
					break;
				case 7:
					startActivity(AdvanceToolsActivity.class);
					break;
				case 8:
					startActivity(SettingsActivity.class);
					break;
				}
			}
		});
		//�ڶ�ҳ
		gridView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Toast.makeText(HomeActivity.this, "�������", 0).show();
					startActivity(BatteryObserveActivity.class);
					break;
				case 1:
//					Toast.makeText(HomeActivity.this, "����2", 0).show();
//					startActivity(APPManagerActivity.class);
					startActivity(CaptureActivity.class);
					break;
				case 2:
//					Toast.makeText(HomeActivity.this, "��������", 0).show();
					break;
				}
			}
		});
		listvp=new ArrayList<View>();
		listvp.add(view1);
		listvp.add(view2);
		mainAdapter=new MainAdapter(listvp);
		viewPager.setAdapter(mainAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
//				Toast.makeText(HomeActivity.this, "��ҳ", 0).show();
				setPage();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//�������Ա
		policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		//����Ȩ��
		ComponentName componentName=new ComponentName(this, MyDeviceAdminReceiver.class);
		//�ж�
		boolean active= policyManager.isAdminActive(componentName);
		if(!active){
			Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "��ȡ��������ԱȨ�ޣ�����Զ��ɾ������");
			startActivity(intent);
		}
	}
	/**
	 * ��ҳ����
	 */
	private void setPage() {
		// TODO Auto-generated method stub
		Animation animation=AnimationUtils.loadAnimation(this, R.anim.page_in);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				int i = viewPager.getCurrentItem();
				if(i==0){
					act_main_pagenum.setBackgroundResource(R.drawable.main_page_one);
				}else{
					act_main_pagenum.setBackgroundResource(R.drawable.main_page_two);
				}
				Animation animation2=AnimationUtils.loadAnimation(HomeActivity.this, R.anim.page_to);
				act_main_pagenum.startAnimation(animation2);
			}
		});
		act_main_pagenum.startAnimation(animation);
	}
	/**
	 * ���õ����Ի���
	 */
	private void showSetupPswdDialog(){
		final SetUpPasswordDialog setUpPasswordDialog=new SetUpPasswordDialog(HomeActivity.this);
		setUpPasswordDialog.setCallBack(new com.graduate.phonesafeguard.chapter02.dialog.SetUpPasswordDialog.MyCallBack() {
			
			@Override
			public void ok() {
				// TODO Auto-generated method stub
				String firstPWDET = setUpPasswordDialog.mFirstPWDET.getText().toString().trim();
				String affirmET = setUpPasswordDialog.mAffirmET.getText().toString().trim();
				if(!TextUtils.isEmpty(firstPWDET) && !TextUtils.isEmpty(affirmET)){
					if(firstPWDET.equals(affirmET)){
						//��������һ������������
						savePassword(affirmET);
						setUpPasswordDialog.dismiss();
						//��ʾ�Ի���
						showInterPswdDialog();
					}else{
						Toast.makeText(getApplicationContext(), "�������벻һ��~", 0).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��Ŷ~", 0).show();
				}
			}
			
			@Override
			public void cancle() {
				// TODO Auto-generated method stub
				setUpPasswordDialog.dismiss();
			}
		});
		setUpPasswordDialog.setCancelable(true);
		setUpPasswordDialog.show();
	}
	//�ж��Ƿ��ǵ�һ�η���
	private boolean isSetUpPassword(){
		String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
		if(!TextUtils.isEmpty(password)){
			return false;
		}
		return true;
	}
	//������������Ի���
	private void showInterPswdDialog(){
		final String password = getPassword();
		final InputPasswordDialog mInputPasswordDialog=new InputPasswordDialog(HomeActivity.this);
		mInputPasswordDialog.setCallBack(new com.graduate.phonesafeguard.chapter02.dialog.InputPasswordDialog.MyCallBack() {
			
			@Override
			public void inputok() {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(mInputPasswordDialog.getPassword())){
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��Ŷ��", 0).show();
				}else if(!password.equals(MD5Utils.encode(mInputPasswordDialog.getPassword()))){
					mInputPasswordDialog.dismiss();
					Toast.makeText(getApplicationContext(), "���벻��Ŷ��~", 0).show();
					
				}else{
					mInputPasswordDialog.dismiss();
					startActivity(LostFindActivity.class);
				}
			}
			
			@Override
			public void disMiss() {
				// TODO Auto-generated method stub
				//�Ի�����ʧ
				mInputPasswordDialog.dismiss();
			}
		});
		mInputPasswordDialog.setCancelable(true);
		mInputPasswordDialog.show();
	}
	//��ȡ����
	private String getPassword(){
		String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
		if(!TextUtils.isEmpty(password)){
			return password;
		}
		return "";
	}
	//��������
	private void savePassword(String affirmPwsd){
		Editor edit = msharedPreferences.edit();
		edit.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwsd));
		edit.commit();
	}
	//��תҳ�淽��
	private void startActivity(Class<?> cls){
		Intent intent=new Intent(HomeActivity.this,cls);
		startActivity(intent);
	}
	//�˳�����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis()-mExitTime)>2000){
				Toast.makeText(this, "�ٰ�һ���˳�����", 0).show();
				mExitTime=System.currentTimeMillis();
			}else{
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startAppad.onResume();//�������Ź��
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		startAppad.onPause();//��ͣ���
	}

	
}
