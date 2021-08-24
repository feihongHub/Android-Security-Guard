package com.graduate.phonesafeguard.chapter01.utils;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultClientConnection;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.graduate.phonesafeguard.HomeActivity;
import com.graduate.phonesafeguard.chapter01.PreActivity;
import com.graduate.phonesafeguard.chapter01.entity.VersionEntity;
import com.graduate.phonesafeguard.chapter01.utils.DownLoadUtils.MyCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.graduate.phonesafeguard.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Handler;
import android.text.AlteredCharSequence;
import android.widget.Toast;

public class VersionUpdateUtils {
	private static final int MESSAGE_SHOW_DIALOG=104;
	private static final int MESSAGE_NET_ERROR=101;
	private static final int MESSAGE_IO_ERROR=102;
	private static final int MESSAGE_JSON_ERROR=103;
	protected static final int MESSAGE_ENTERHOME=105;
	//测试
	private static final int MESSAGE_ENTERPREVIEW=106;
	private String mVersionName;
	private int mVersion;
	private Activity context;
	private VersionEntity versionEntity;
	private SharedPreferences share;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOW_DIALOG:
				//显示对话框
				showUpdateDialog(versionEntity);
				break;
			case MESSAGE_NET_ERROR:
				Toast.makeText(context, "网络地址有误", 0).show();
				enterHome();
				break;
			case MESSAGE_IO_ERROR:
				Toast.makeText(context, "网络连接中断", 0).show();
				enterHome();
				break;
			case MESSAGE_JSON_ERROR:
				Toast.makeText(context, "数据解析异常", 0).show();
				enterHome();
				break;
			case MESSAGE_ENTERHOME:
				Intent intent=new Intent(context,HomeActivity.class);
				context.startActivity(intent);
				context.finish();
				break;
			//测试类
			case MESSAGE_ENTERPREVIEW:
				Intent intent2=new Intent(context,PreActivity.class);
				context.startActivity(intent2);
				context.finish();
				break;
			}
		};
	};
	private ProgressDialog mprogressDialog;
	/**
	 * 本地版本号
	 * @param Version
	 * @param activity
	 */
	public VersionUpdateUtils(int Version,Activity activity,SharedPreferences share){
		mVersion=Version;
		context=activity;
		this.share=share;
	}
	/**
	 * 请求服务器版本
	 */
	public void getCloudVersion(){
		HttpClient client=new DefaultHttpClient();
		//链接超时
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
		//请求超时
		HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
		//可以通过tomcat进行适当性修改
		HttpGet httpGet=new HttpGet("http://172.26.59.1:8080/updateinfo1.html");
//		HttpGet httpGet=new HttpGet("http://172.26.59.1:8080/updateinfo.json");
		try {
			HttpResponse execute = client.execute(httpGet);
			if(execute.getStatusLine().getStatusCode()==200){
				//请求响应都成功
				HttpEntity entity = execute.getEntity();
				String result = EntityUtils.toString(entity,"utf-8");
				//创建jsonObject对象
				JSONObject jsonObject=new JSONObject(result);
				versionEntity = new VersionEntity();
				String codeName=jsonObject.getString("codename");
				versionEntity.versionName=codeName;
				int code = jsonObject.getInt("code");
				versionEntity.VersionCode=code;
				String des = jsonObject.getString("des");
				versionEntity.description=des;
				String apkurl = jsonObject.getString("apkurl");
				versionEntity.apkurl=apkurl;
				if( mVersion < versionEntity.VersionCode){
					handler.sendEmptyMessage(MESSAGE_SHOW_DIALOG);
				}else if(mVersion == versionEntity.VersionCode){
					handler.sendEmptyMessage(MESSAGE_ENTERHOME);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			//网络连接中断
			handler.sendEmptyMessage(MESSAGE_NET_ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			//IO异常
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(MESSAGE_IO_ERROR);
			e.printStackTrace();
		} catch (JSONException e) {
			//json请求异常
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(MESSAGE_JSON_ERROR);
			e.printStackTrace();
		}
	}
	/**
	 * 弹出更新对话框
	 */
	private void showUpdateDialog(final VersionEntity versionEntity){
		AlertDialog.Builder builder=new Builder(context);
		//从VersionEntity的对象中获取包名
		builder.setTitle("检查到新版本"+versionEntity.versionName);
		builder.setMessage(versionEntity.description);
		builder.setCancelable(false);//设置不能点击手机返回按钮隐藏对话框
		builder.setIcon(R.drawable.ic_launcher);//注意R.drawable.引用不了图片就看R包问题
		builder.setPositiveButton("立即升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				initProgressDialog();
				downloadNewApk(versionEntity.apkurl);
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();//显示对话框
		
	}
	/**
	 * 初始化进度条对话框
	 */
	private void initProgressDialog(){
		mprogressDialog = new ProgressDialog(context);
		mprogressDialog.setMessage("准备下载...");
		mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置条形对话框样式
		mprogressDialog.show();//显示
	}
	/**
	 * 下载新版本
	 */
	protected void downloadNewApk(String apkurl){
		DownLoadUtils downLoadUtils=new DownLoadUtils();
		downLoadUtils.downapk(apkurl, "/mnt/sdcard/safe2.apk", new MyCallBack() {
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub
				mprogressDialog.dismiss();
				//开始安装
				MyUtils.installApk(context);
			}
			
			@Override
			public void onLoadding(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				mprogressDialog.setMax((int) total);
				mprogressDialog.setMessage("正在下载...");
				mprogressDialog.setProgress((int) current);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				mprogressDialog.setMessage("下载失败");
				mprogressDialog.dismiss();
				enterHome();
			}
		});
	}
	public void enterHome(){
		share=context.getSharedPreferences("home_enter",Context.MODE_PRIVATE );
		String content = share.getString("orenter", "");
//		Toast.makeText(context, content, 0).show();
		if(content.equals("yes")){
//			Toast.makeText(context, "执行啊", 0).show();
			handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
		}else{
			handler.sendEmptyMessageDelayed(MESSAGE_ENTERPREVIEW, 2000);
		}
			
		
		
	}
}
