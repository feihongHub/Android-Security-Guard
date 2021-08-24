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
	//����
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
				//��ʾ�Ի���
				showUpdateDialog(versionEntity);
				break;
			case MESSAGE_NET_ERROR:
				Toast.makeText(context, "�����ַ����", 0).show();
				enterHome();
				break;
			case MESSAGE_IO_ERROR:
				Toast.makeText(context, "���������ж�", 0).show();
				enterHome();
				break;
			case MESSAGE_JSON_ERROR:
				Toast.makeText(context, "���ݽ����쳣", 0).show();
				enterHome();
				break;
			case MESSAGE_ENTERHOME:
				Intent intent=new Intent(context,HomeActivity.class);
				context.startActivity(intent);
				context.finish();
				break;
			//������
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
	 * ���ذ汾��
	 * @param Version
	 * @param activity
	 */
	public VersionUpdateUtils(int Version,Activity activity,SharedPreferences share){
		mVersion=Version;
		context=activity;
		this.share=share;
	}
	/**
	 * ����������汾
	 */
	public void getCloudVersion(){
		HttpClient client=new DefaultHttpClient();
		//���ӳ�ʱ
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
		//����ʱ
		HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
		//����ͨ��tomcat�����ʵ����޸�
		HttpGet httpGet=new HttpGet("http://172.26.59.1:8080/updateinfo1.html");
//		HttpGet httpGet=new HttpGet("http://172.26.59.1:8080/updateinfo.json");
		try {
			HttpResponse execute = client.execute(httpGet);
			if(execute.getStatusLine().getStatusCode()==200){
				//������Ӧ���ɹ�
				HttpEntity entity = execute.getEntity();
				String result = EntityUtils.toString(entity,"utf-8");
				//����jsonObject����
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
			//���������ж�
			handler.sendEmptyMessage(MESSAGE_NET_ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			//IO�쳣
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(MESSAGE_IO_ERROR);
			e.printStackTrace();
		} catch (JSONException e) {
			//json�����쳣
			// TODO Auto-generated catch block
			handler.sendEmptyMessage(MESSAGE_JSON_ERROR);
			e.printStackTrace();
		}
	}
	/**
	 * �������¶Ի���
	 */
	private void showUpdateDialog(final VersionEntity versionEntity){
		AlertDialog.Builder builder=new Builder(context);
		//��VersionEntity�Ķ����л�ȡ����
		builder.setTitle("��鵽�°汾"+versionEntity.versionName);
		builder.setMessage(versionEntity.description);
		builder.setCancelable(false);//���ò��ܵ���ֻ����ذ�ť���ضԻ���
		builder.setIcon(R.drawable.ic_launcher);//ע��R.drawable.���ò���ͼƬ�Ϳ�R������
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				initProgressDialog();
				downloadNewApk(versionEntity.apkurl);
			}
		});
		builder.setNegativeButton("�´���˵", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();//��ʾ�Ի���
		
	}
	/**
	 * ��ʼ���������Ի���
	 */
	private void initProgressDialog(){
		mprogressDialog = new ProgressDialog(context);
		mprogressDialog.setMessage("׼������...");
		mprogressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//�������ζԻ�����ʽ
		mprogressDialog.show();//��ʾ
	}
	/**
	 * �����°汾
	 */
	protected void downloadNewApk(String apkurl){
		DownLoadUtils downLoadUtils=new DownLoadUtils();
		downLoadUtils.downapk(apkurl, "/mnt/sdcard/safe2.apk", new MyCallBack() {
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub
				mprogressDialog.dismiss();
				//��ʼ��װ
				MyUtils.installApk(context);
			}
			
			@Override
			public void onLoadding(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				mprogressDialog.setMax((int) total);
				mprogressDialog.setMessage("��������...");
				mprogressDialog.setProgress((int) current);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				mprogressDialog.setMessage("����ʧ��");
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
//			Toast.makeText(context, "ִ�а�", 0).show();
			handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
		}else{
			handler.sendEmptyMessageDelayed(MESSAGE_ENTERPREVIEW, 2000);
		}
			
		
		
	}
}
