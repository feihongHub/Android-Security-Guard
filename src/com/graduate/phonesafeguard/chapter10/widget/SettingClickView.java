package com.graduate.phonesafeguard.chapter10.widget;

import com.graduate.phonesafeguard.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
/**
 * ����Զ���ؼ�
 * @author Administrator
 */
public class SettingClickView extends RelativeLayout {
//	private static final String nameSpace = "http://schemas.android.com/apk/res/com.graduate.phonesafeguard";
	private TextView tvTitle;
	private TextView tvStatus;

	public SettingClickView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	public SettingClickView(Context context,AttributeSet attrs){
		super(context,attrs);
		init(context);

//		String setTitle = attrs.getAttributeValue(nameSpace, "settitle");
//		tvTitle.setText(setTitle);
	}
	public SettingClickView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		init(context);
	}
	/**
	 * ��ʼ���ؼ�
	 */
	private void init(Context context){
		View child = View.inflate(context, R.layout.ui_settingclick_view, null);
		tvTitle = (TextView) child.findViewById(R.id.tv_setting_title_click);
		tvStatus = (TextView) child.findViewById(R.id.tv_setting_status_click);
		this.addView(child);//���ӽڵ���ӵ�������	
	}
	//���ñ���
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	//��������
	public void setStatus(String status){
		tvStatus.setText(status);
	}
}
