package com.graduate.phonesafeguard.chapter09;

import java.util.ArrayList; 
import java.util.List;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter09.Fragment.AppLockFragment;
import com.graduate.phonesafeguard.chapter09.Fragment.AppUnLockFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 完成两个Fragment切换
 * @author Administrator
 *
 */
public class AppLockActivity extends FragmentActivity implements OnClickListener {
	//关联源码
	private ViewPager mAppViewPager;
	List<Fragment> mFragments=new ArrayList<Fragment>();
	private TextView mLockTV;
	private TextView mUnLockTV;
	private View slideLockView;
	private View sildeUnLockView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_applock);
		initView();
		initListener();
	}
	@SuppressWarnings("deprecation")
	private void initListener() {
		// TODO Auto-generated method stub
		mAppViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position==0){
					sildeUnLockView.setBackgroundResource(R.drawable.slide_view);
					slideLockView.setBackgroundColor(getResources().getColor(R.color.transparent));
					//未加锁
					mLockTV.setTextColor(getResources().getColor(R.color.black));
					mUnLockTV.setTextColor(getResources().getColor(R.color.deep_red));
				}else{
					slideLockView.setBackgroundResource(R.drawable.slide_view);
					sildeUnLockView.setBackgroundColor(getResources().getColor(R.color.transparent));

					//加锁
					mLockTV.setTextColor(getResources().getColor(R.color.deep_red));
					mUnLockTV.setTextColor(getResources().getColor(R.color.black));
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				//接口回调
			}
		});
	}
	//初始化方法
	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.deep_red));
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView)findViewById(R.id.tv_title)).setText("程序锁");
		mLeftImgv.setImageResource(R.drawable.title_back);
		mLeftImgv.setOnClickListener(this);
		mAppViewPager = (ViewPager) findViewById(R.id.vp_applock);
		mLockTV = (TextView) findViewById(R.id.tv_lock);
		mUnLockTV = (TextView) findViewById(R.id.tv_unlock);
		mLockTV.setOnClickListener(this);
		mUnLockTV.setOnClickListener(this);
		slideLockView = findViewById(R.id.view_slide_lock);
		sildeUnLockView = findViewById(R.id.view_slide_nulock);
		AppLockFragment lock=new AppLockFragment();
		AppUnLockFragment unlock=new AppUnLockFragment();
		mFragments.add(unlock);
		mFragments.add(lock);
		mAppViewPager.setAdapter(new Myadapter(getSupportFragmentManager()));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.tv_lock:
			mAppViewPager.setCurrentItem(1);
			break;
		case R.id.tv_unlock:
			mAppViewPager.setCurrentItem(0);
			break;
		}
	}
	class Myadapter extends FragmentPagerAdapter{

		public Myadapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mFragments.size();
		}
		
	}

}
