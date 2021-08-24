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
// android:theme="@android:style/Theme.Translucent.NoTitleBar"��͸��
public class DragViewActivity extends Activity implements OnClickListener {

	private TextView tvDrag;
	private int startX;
	private int startY;
	private SharedPreferences mSp;
	private int wmwidth;
	private int wmheight;
	private TextView tvSettingTop;
	private TextView tvSettingButtom;
	private long []mHit=new long[2];//˫���¼�
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
		//����ȡ����Ĳ���ʱ��Ӧ���ı����ı���λ��
		if(lastY>wmheight/2){
			tvSettingTop.setVisibility(View.VISIBLE);
			tvSettingButtom.setVisibility(View.INVISIBLE);
		}else{
			tvSettingTop.setVisibility(View.INVISIBLE);
			tvSettingButtom.setVisibility(View.VISIBLE);
		}
		//measure(�������)->layout(�趨λ��)->draw(����)�����������������oncreate����������ŵ���
//		tvDrag.layout(lastX, lastY, lastX+tvDrag.getWidth(), lastY+tvDrag.getHeight());//�˷���������oncreate�����С�
		//��Ϊ���ֻ������̻�δ����
		//ͨ���޸Ĳ��ֲ����޸�λ��
		//���ֵĸ�����˭���ͻ�ȡ˭�Ĳ��ֲ���
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) tvDrag.getLayoutParams();
		//�޸Ĳ��ֲ���ֵ
		params.topMargin=lastY;
		params.leftMargin=lastX;
		tvDrag.setOnClickListener(this);
		//���ô����¼�����
		tvDrag.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					System.out.println("����");
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					System.out.println("�ƶ�");
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx=endX-startX;
					int dy=endY-startY;
					//����ƫ��������λ��
					int l=tvDrag.getLeft()+dx;
					int r=tvDrag.getRight()+dx;
					int t=tvDrag.getTop()+dy;
					int b=tvDrag.getBottom()+dy;
					//��ֹ��Ļ�����߽�
					if(l<0 || r>wmwidth){
						return true;
					}
					//��ֹ��Ļ�����߽�
					if(t<0 || b>wmheight-30){
						return true;
					}
					//���ݵ�ǰλ����ʾ�ı���
					if(t>wmheight/2){
						tvSettingTop.setVisibility(View.VISIBLE);
						tvSettingButtom.setVisibility(View.INVISIBLE);
					}else{
						tvSettingTop.setVisibility(View.INVISIBLE);
						tvSettingButtom.setVisibility(View.VISIBLE);
					}
					tvDrag.layout(l, t, r, b);
					//���³�ʼ����ʼ����
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					System.out.println("̧��");
					//����
					mSp.edit().putInt("lastX", tvDrag.getLeft()).commit();
					mSp.edit().putInt("lastY", tvDrag.getTop()).commit();
					break;
				}
				//�������¼����뷵��true
				//����onclick��OnTouch�¼����淵��false
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
			mHit[mHit.length-1]= SystemClock.uptimeMillis();//�ֻ�����ʱ��
			if(SystemClock.uptimeMillis()-mHit[0]<=500){
				tvDrag.layout(wmwidth/2-tvDrag.getWidth()/2, tvDrag.getTop(), wmwidth/2+tvDrag.getWidth()/2, tvDrag.getBottom());
			}
			break;
		}
	}

}
