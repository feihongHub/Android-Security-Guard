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
 * 组合自定义控件
 * @author Administrator
 */
public class SettingView extends RelativeLayout {
	private static final String nameSpace = "http://schemas.android.com/apk/res/com.graduate.phonesafeguard";
	private TextView tvTitle;
	private TextView tvStatus;
	private CheckBox cbcheck;
	private String statusOn;
	private String statusOff;
	public SettingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	public SettingView(Context context,AttributeSet attrs){
		super(context,attrs);
		init(context);
//		int count = attrs.getAttributeCount();
//		for (int i=0;i<count;i++) {
//			attrs.getAttributeName(i);
//			attrs.getAttributeValue(i);
//		}
		String setTitle = attrs.getAttributeValue(nameSpace, "settitle");
		statusOn = attrs.getAttributeValue(nameSpace, "status_on");
		statusOff = attrs.getAttributeValue(nameSpace, "status_off");
		tvTitle.setText(setTitle);
	}
	public SettingView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		init(context);
	}
	/**
	 * 初始化控件
	 */
	private void init(Context context){
		View child = View.inflate(context, R.layout.ui_settings_view, null);
		tvTitle = (TextView) child.findViewById(R.id.tv_setting_title_check);
		tvStatus = (TextView) child.findViewById(R.id.tv_setting_status_check);
		cbcheck = (CheckBox) child.findViewById(R.id.tv_settings_check_box);

		this.addView(child);//将子节点添加到载体里	
	}
	//设置标题
	public void setTitle(String title){
		tvTitle.setText(title);
	}
	//设置描述
	public void setStatus(String status){
		tvStatus.setText(status);
	}
	//判断是否勾选
	public boolean isChecked(){
		return cbcheck.isChecked();
	}
	public void setChecked(boolean checked){
		cbcheck.setChecked(checked);
		if(checked){
			tvStatus.setText(statusOn);
		}else{
			tvStatus.setText(statusOff);
		}
	}
}
