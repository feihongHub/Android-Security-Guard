package com.graduate.phonesafeguard.chapter08.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;

/**
 * <pre>
 * ҵ����:
 * ����˵��: 
 * ��д����:	2016-7-30
 * 
 * ��ʷ��¼
 * 1���޸����ڣ�
 *    �޸��ˣ�
 *    �޸����ݣ�
 * </pre>
 */
public class MyDatePickerDialog extends DatePickerDialog {
	//ʵ���������췽��
	public MyDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);

	}

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		setButton("ȷ��", this);
	}

}
