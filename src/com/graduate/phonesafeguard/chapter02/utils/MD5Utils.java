package com.graduate.phonesafeguard.chapter02.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public  static String encode(String text){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(text.getBytes());
			StringBuilder builder=new StringBuilder();
			for(byte b: result){
				int number=b&0xff;
				String hexString = Integer.toHexString(number);
				if(hexString.length()==1){
					builder.append("0"+hexString);
				}else{
					builder.append(hexString);
				}
			}
			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
