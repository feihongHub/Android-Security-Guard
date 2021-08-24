package com.graduate.phonesafeguard.chapter03.entity;

public class BlackContactInfo {
	public String phoneNumber;//联系人电话
	public String contactName;//联系人姓名
	/**
	 * 黑名单模式      1 电话拦截   2短信拦截   3电话，短信拦截
	 */
	public int mode;//黑名单中的状态
//	public int id;//黑名单id
	//
	public String getModeString(int mode){
		switch (mode) {
		case 1:	
			return "电话拦截";
		case 2:	
			return "短信拦截";
		case 3:	
			return "电话、短信拦截";

		}
		return "";
		
	}
	
}
