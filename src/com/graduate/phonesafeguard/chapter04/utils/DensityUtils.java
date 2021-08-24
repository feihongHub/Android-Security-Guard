package com.graduate.phonesafeguard.chapter04.utils;

import android.content.Context;

public class DensityUtils {
	/**
	 * dipת������px
	 */
	public static int dip2px(Context context,float dpValue){
		try {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int)(dpValue*scale+0.5f);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return (int) dpValue;
		
	}
	/**
	 * ����pxת��Ϊdip
	 */
	public static int px2dip(Context context,float pxValue){
		try {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int)(pxValue/scale+0.5f);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return (int)pxValue;
		
	}
}
