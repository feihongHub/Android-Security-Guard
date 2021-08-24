package com.graduate.phonesafeguard.chapter08.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2016-7-30
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class MyDatePickerDialog extends DatePickerDialog {
	//实现两个构造方法
	public MyDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);

	}

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		setButton("确定", this);
	}

}
