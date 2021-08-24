package com.graduate.phonesafeguard.chapter01;

import java.util.ArrayList; 
import java.util.List;

import com.graduate.phonesafeguard.HomeActivity;
import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter01.adapter.SetpagerAdapter;

import android.R.array;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PreActivity extends Activity{
	private SetpagerAdapter adapter;
	private List<View> list;
	private ArrayList<View> arrayList;
	private View view1,view2,view3,view4;
	private ViewPager viewPager;
	private int currentItem;
	private int oldItem=0;
	private ImageView image;
	private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
	private Button button;
	private SharedPreferences share,share2;
	private long mExitTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//更改窗口特性
		setContentView(R.layout.activity_previews);


		init();
	}
	@SuppressWarnings("deprecation")
	private void init() {
		// TODO Auto-generated method stub
		arrayList=new ArrayList<View>();
		arrayList.add(findViewById(R.id.preview1));
		arrayList.add(findViewById(R.id.preview2));
		arrayList.add(findViewById(R.id.preview3));
		arrayList.add(findViewById(R.id.preview4));

		LayoutInflater inflater=LayoutInflater.from(PreActivity.this);
		list=new ArrayList<View>();
		view1 = inflater.inflate(R.layout.spl_viewpager1, null);
		view2=inflater.inflate(R.layout.spl_viewpager2, null);
		view3=inflater.inflate(R.layout.spl_viewpager3, null);
		view4=inflater.inflate(R.layout.spl_viewpager4, null);


		list.add(view1);
		list.add(view2);
		list.add(view3);
		list.add(view4);

		adapter=new SetpagerAdapter(PreActivity.this, list);
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);
		arrayList.get(oldItem).setBackgroundResource(R.drawable.pre_circleon);
		viewPager.setAdapter(adapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub

				currentItem=position;
				arrayList.get(oldItem).setBackgroundResource(R.drawable.pre_circleoff);
				arrayList.get(position).setBackgroundResource(R.drawable.pre_circleon);
				oldItem=currentItem;
				switch (position) {
				case 1:
					image=(ImageView) view2.findViewById(R.id.spl_view2image);
					Animation animation1=AnimationUtils.loadAnimation(PreActivity.this, R.anim.scal);
					image.startAnimation(animation1);
					break;
				case 2:
					tv1=(TextView) view3.findViewById(R.id.spl_view3tv1);
					tv2=(TextView) view3.findViewById(R.id.spl_view3tv2);
					tv3=(TextView) view3.findViewById(R.id.spl_view3tv3);
					tv4=(TextView) view3.findViewById(R.id.spl_view3tv4);
					tv5=(TextView) view3.findViewById(R.id.spl_view3tv5);
					tv6=(TextView) view3.findViewById(R.id.spl_view3tv6);
					tv7=(TextView) view3.findViewById(R.id.spl_view3tv7);
					share=getSharedPreferences("vp3", MODE_PRIVATE);
					String content = share.getString("vp", "");
					if(content.equals("yes")){
						break;
					}

					final Animation animation2=AnimationUtils.loadAnimation(PreActivity.this, R.anim.tran);
					final Animation anim1=AnimationUtils.loadAnimation(PreActivity.this, R.anim.tranone);
					final Animation anim=AnimationUtils.loadAnimation(PreActivity.this, R.anim.trantwo);
					tv1.startAnimation(animation2);


					animation2.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							tv1.setVisibility(View.VISIBLE);

						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							tv2.startAnimation(anim1);
							tv2.setVisibility(View.VISIBLE);
							tv3.startAnimation(anim1);
							tv3.setVisibility(View.VISIBLE);
							tv4.startAnimation(anim1);
							tv4.setVisibility(View.VISIBLE);
							tv5.startAnimation(anim);
							tv5.setVisibility(View.VISIBLE);
							tv6.startAnimation(anim);
							tv6.setVisibility(View.VISIBLE);
							tv7.startAnimation(anim);
							tv7.setVisibility(View.VISIBLE);
							Editor editor=share.edit();
							editor.putString("vp", "yes");
							editor.commit();

						}
					});
					break;
				case 3:

					button=(Button) view4.findViewById(R.id.spl_viewButton);
					tv8=(TextView) view4.findViewById(R.id.spl_view4text);
					Animation animation3=AnimationUtils.loadAnimation(PreActivity.this, R.anim.alphy);
					tv8.startAnimation(animation3);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							share2=getSharedPreferences("home_enter", MODE_PRIVATE);
							Editor editor = share2.edit();
							editor.putString("orenter", "yes");
							editor.commit();

							Intent intent=new Intent(PreActivity.this,HomeActivity.class);
							startActivity(intent);
							finish();
						}
					});
					break;
				}
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

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK )  
		{  
			if((System.currentTimeMillis()-mExitTime)>2000){
				Toast.makeText(this, "再按一下退出程序", 0).show();
				mExitTime=System.currentTimeMillis();
			}else{
				System.exit(0);
			}
			return true;
		}  
		return super.onKeyDown(keyCode, event);
	}

}
