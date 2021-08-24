package com.graduate.phonesafeguard.chapter01.utils;

import java.io.File;
import java.net.InterfaceAddress;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class DownLoadUtils {
	/**
	 * 使用xutils下载
	 */
	public void downapk(String url,String targerFile,final MyCallBack myCallBack){
		HttpUtils httpUtils=new HttpUtils();//创建xutils对象
		httpUtils.download(url, targerFile, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub
				myCallBack.onSuccess(responseInfo);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				myCallBack.onFailure(error, msg);
			}
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
				myCallBack.onLoadding(total, current, isUploading);
			}
		});
	}
	//方法回调
	interface MyCallBack{
		void onSuccess(ResponseInfo<File> responseInfo);
		void onFailure(HttpException error, String msg);
		void onLoadding(long total, long current, boolean isUploading);
	}
}
