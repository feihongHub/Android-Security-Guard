package com.graduate.phonesafeguard.chapter09;

import com.graduate.phonesafeguard.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
// android:theme="@android:style/Theme.Translucent.NoTitleBar"半透明
public class DragViewActivity extends Activity implements OnClickListener {

	private TextView tvDrag;
	private int startX;
	private int startY;
	private SharedPreferences mSp;
	private int wmwidth;
	private int wmheight;
	private TextView tvSettingTop;
	private TextView tvSettingButtom;
	private long []mHit=new long[2];//双击事件
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_drag_view);
		mSp=getSharedPreferences("config", MODE_PRIVATE);
		WindowManager wm=(WindowManager) getSystemService(WINDOW_SERVICE);
		wmwidth = wm.getDefaultDisplay().getWidth();
		wmheight = wm.getDefaultDisplay().getHeight();
//		AlphaAnimation animation=new AlphaAnimation(0.1f, 1);
//		animation.setDuration(1000);
//		animation.start();
		initView();
	}
	private void initView(){
		tvDrag = (TextView) findViewById(R.id.tv_locationset);
		tvSettingTop = (TextView) findViewById(R.id.textView1);
		tvSettingButtom = (TextView) findViewById(R.id.textView2);
		int lastX = mSp.getInt("lastX", 0);
		int lastY = mSp.getInt("lastY", 0);
		//当获取保存的参数时，应当改变其文本框位置
		if(lastY>wmheight/2){
			tvSettingTop.setVisibility(View.VISIBLE);
			tvSettingButtom.setVisibility(View.INVISIBLE);
		}else{
			tvSettingTop.setVisibility(View.INVISIBLE);
			tvSettingButtom.setVisibility(View.VISIBLE);
		}
		//measure(测量宽高)->layout(设定位置)->draw(绘制)，这三个步骤必须在oncreate方法结束后才调用
//		tvDrag.layout(lastX, lastY, lastX+tvDrag.getWidth(), lastY+tvDrag.getHeight());//此方法不能在oncreate中运行。
		//因为布局绘制流程还未启动
		//通过修改布局参数修改位置
		//布局的父类是谁，就获取谁的布局参数
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) tvDrag.getLayoutParams();
		//修改布局参数值
		params.topMargin=lastY;
		params.leftMargin=lastX;
		tvDrag.setOnClickListener(this);
		//设置触摸事件监听
		tvDrag.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out.println("按下");
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					System.out.println("移动");
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx=endX-startX;
					int dy=endY-startY;
					//根据偏移量更新位置
					int l=tvDrag.getLeft()+dx;
					int r=tvDrag.getRight()+dx;
					int t=tvDrag.getTop()+dy;
					int b=tvDrag.getBottom()+dy;
					//防止屏幕超出边界
					if(l<0 || r>wmwidth){
						return true;
					}
					//防止屏幕超出边界
					if(t<0 || b>wmheight-30){
						return true;
					}
					//根据当前位置显示文本框
					if(t>wmheight/2){
						tvSettingTop.setVisibility(View.VISIBLE);
						tvSettingButtom.setVisibility(View.INVISIBLE);
					}else{
						tvSettingTop.setVisibility(View.INVISIBLE);
						tvSettingButtom.setVisibility(View.VISIBLE);
					}
					tvDrag.layout(l, t, r, b);
					//重新初始化起始坐标
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("抬起");
					//保存
					mSp.edit().putInt("lastX", tvDrag.getLeft()).commit();
					mSp.edit().putInt("lastY", tvDrag.getTop()).commit();
					break;
				}
				//消耗子事件必须返回true
				//但若onclick与OnTouch事件共存返回false
				return false;
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_locationset:
			System.arraycopy(mHit, 1, mHit, 0,mHit.length-1);
			mHit[mHit.length-1]= SystemClock.uptimeMillis();//手机开机时间
			if(SystemClock.uptimeMillis()-mHit[0]<=500){
				tvDrag.layout(wmwidth/2-tvDrag.getWidth()/2, tvDrag.getTop(), wmwidth/2+tvDrag.getWidth()/2, tvDrag.getBottom());
			}
			break;
		}
	}

}
