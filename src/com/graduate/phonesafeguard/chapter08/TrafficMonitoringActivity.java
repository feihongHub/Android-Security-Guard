package com.graduate.phonesafeguard.chapter08;

import java.text.SimpleDateFormat; 

import java.util.Calendar;
import java.util.Date;

import com.graduate.phonesafeguard.R;
import com.graduate.phonesafeguard.chapter08.db.DataSupport;
import com.graduate.phonesafeguard.chapter08.utils.MyDatePickerDialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2012-3-16
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class TrafficMonitoringActivity extends Activity{

	private TextView mg_up_total, mg_down_total, mg_total, mUp_total,
	mDown_total, mliuliang_total, date_start_textview,
	date_over_textview, liuliangzongbiaoti;
	private Button date_start_btn, date_over_btn, search_btn, detail_data_app,show_now_button;
	private int id_number_r = 0, id_number_t = 0;
	public static final String RXG = "rxg";
	public static final String TXG = "txg";
	public static final String RX = "rx";
	public static final String TX = "tx";
	public static final String SHUTDOWN = "d";
	public static final String NORMAL = "n";
	public static final String RX3G = "3g下载流量";
	public static final String TX3G = "3g上传流量";
	public static final String RXT = "下载总流量";
	public static final String TXT = "上传总流量";
	public static final String flag = "first";
	public static final String flagname = "nomber1";
	public static boolean isLog = false;
	private DataSupport minsert = new DataSupport(this);
	private Calendar calendar = Calendar.getInstance();
	private Calendar mcalendar = Calendar.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//更改窗口特性
		setContentView(R.layout.activity_traffic_main);
		init();
		datainsert();
		showdata();
		alarmsave();
	}

	/**
	 * 
	 * 方法说明：初始化应用程序
	 * 
	 */
	private void init() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
		((TextView)findViewById(R.id.tv_title)).setText("流量统计");
		ImageView mLeftImgv=(ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mLeftImgv.setImageResource(R.drawable.title_back);
		mg_down_total = (TextView) findViewById(R.id.g_down_edit);
		mg_up_total = (TextView) findViewById(R.id.g_up_edit);
		mg_total = (TextView) findViewById(R.id.g_total_edit);
		mUp_total = (TextView) findViewById(R.id.total_up_edit);
		mDown_total = (TextView) findViewById(R.id.total_down_edit);
		mliuliang_total = (TextView) findViewById(R.id.liuliang_total_edit);
		liuliangzongbiaoti = (TextView) findViewById(R.id.liuliang_biaoti);
		date_over_btn = (Button) findViewById(R.id.date_over_btn);
		date_start_btn = (Button) findViewById(R.id.date_start_btn);
		search_btn = (Button) findViewById(R.id.search_button);
		detail_data_app = (Button) findViewById(R.id.chakan_apps_data);
		show_now_button=(Button)findViewById(R.id.shownow_button);
		show_now_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				liuliangzongbiaoti.setText(getResources().getString(R.string.now_liuliang_total));
				showdata();	
			}
		});
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		date_over_textview = (TextView) findViewById(R.id.date_over);
		date_over_textview.setText(sdf.format(new Date()));
		date_start_textview = (TextView) findViewById(R.id.date_start);
		date_start_textview.setText(sdf.format(new Date()));

		detail_data_app.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(TrafficMonitoringActivity.this, TrafficDataShowActivity.class);
				startActivity(it);
			}
		});
		date_over_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectdate(date_over_textview);

			}
		});
		date_start_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectdate(date_start_textview);

			}
		});
		search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search_result();

			}
		});
	}

	/**
	 * 
	 * 方法说明：两个日期之间进行搜索
	 * 
	 */
	private void search_result() {
		long g3down = 0, g3up = 0, g3total = 0, rxdown = 0, txup = 0, alltotal = 0;
		String startdate = date_start_textview.getText().toString();
		String overdate = date_over_textview.getText().toString();
		Cursor r3gst = minsert.selectBettweenstart(startdate, overdate, RX3G);
		Cursor r3gov = minsert.selectBettweenstop(startdate, overdate, RX3G);
		Cursor t3gst = minsert.selectBettweenstart(startdate, overdate, TX3G);
		Cursor t3gov = minsert.selectBettweenstop(startdate, overdate, TX3G);
		Cursor r3gshutdown = minsert.selectbetweenday(RX3G, SHUTDOWN,
				startdate, overdate);
		Cursor t3gshutdown = minsert.selectbetweenday(TX3G, SHUTDOWN,
				startdate, overdate);
		liuliangzongbiaoti.setText(startdate + "至" + overdate + "的流量");
		if (r3gst.moveToNext()) {
			int number = r3gst.getColumnIndex("traffic");
			g3down = r3gst.getLong(number);
			int number_id_r3s = r3gst.getColumnIndex("_id");
			id_number_r = r3gst.getInt(number_id_r3s);
			if (TrafficMonitoringActivity.isLog) {
				Log.i("traffic", "rxsearch_result_start_between_rx3g>>>"
						+ g3down + ">>>>>>>uppstart");
			}
			if (r3gov.moveToNext()) {
				int number_id_r3o = r3gov.getColumnIndex("_id");
				id_number_t = r3gov.getInt(number_id_r3o);
				if (id_number_r == id_number_t) {
					
					Toast.makeText(this, "一样哦", 1).show();
				} else {
					int number1 = r3gov.getColumnIndex("traffic");
					g3down = r3gov.getLong(number1) - g3down;
				}
			}
			while (r3gshutdown.moveToNext()) {
				int number2 = r3gshutdown.getColumnIndex("traffic");
				g3down = r3gshutdown.getLong(number2) + g3down;
			}
			g3down = g3down / 1024 / 1024;
			mg_down_total.setText(g3down + "MB");
		}
		if (t3gst.moveToNext()) {
			int number3 = t3gst.getColumnIndex("traffic");
			g3up = t3gst.getLong(number3);
			int number_id_t3s = t3gst.getColumnIndex("_id");
			id_number_r = t3gst.getInt(number_id_t3s);

			if (t3gov.moveToNext()) {
				int number_id_t3o = t3gov.getColumnIndex("_id");
				id_number_t = t3gov.getInt(number_id_t3o);
				if (id_number_r == id_number_t) {
				} else {
					int number4 = t3gov.getColumnIndex("traffic");
					g3up = t3gov.getLong(number4) - g3up;
				}
			}
			while (t3gshutdown.moveToNext()) {
				int number5 = t3gshutdown.getColumnIndex("traffic");
				g3up = g3up + t3gshutdown.getLong(number5);
			}
			g3up = g3up / 1024 / 1024;
			mg_up_total.setText(g3up + "MB");
			g3total = g3down + g3up;
			mg_total.setText(g3total + "MB");

		}
		Cursor rst = minsert.selectBettweenstart(startdate, overdate, RX);
		Cursor rov = minsert.selectBettweenstop(startdate, overdate, RX);
		Cursor tst = minsert.selectBettweenstart(startdate, overdate, TX);
		Cursor tov = minsert.selectBettweenstop(startdate, overdate, TX);
		Cursor rshutdown = minsert.selectbetweenday(RX, SHUTDOWN, startdate,
				overdate);
		Cursor tshutdown = minsert.selectbetweenday(TX, SHUTDOWN, startdate,
				overdate);
		if (rst.moveToNext()) {
			int number6 = rst.getColumnIndex("traffic");
			rxdown = rst.getLong(number6);
			int number_id_rs = rst.getColumnIndex("_id");
			id_number_r = rst.getInt(number_id_rs);

			if (TrafficMonitoringActivity.isLog) {
				Log.i("liuliang", "rxsearch_result_start_rxrx>>>" + rxdown
						+ ">>>>>>>uppstart");
			}
			if (rov.moveToNext()) {
				int number_id_ro = rov.getColumnIndex("_id");
				id_number_t = rst.getInt(number_id_ro);
				if (id_number_r == id_number_t) {
				} else {
					int number7 = rov.getColumnIndex("traffic");
					rxdown = rov.getLong(number7) - rxdown;

					if (TrafficMonitoringActivity.isLog) {
						Log.i("liuliang", "rxsearch_result_rx_stop_ssss>>>>"
								+ rxdown + ">>>>>>uppstart");
						Log.i("liuliang",
								"rxsearch_result_rx_stop_ssss_resa>>>>"
										+ rov.getLong(number7)
										+ ">>>>>>uppstart");
					}
				}
			}
			while (rshutdown.moveToNext()) {
				int number8 = rshutdown.getColumnIndex("traffic");
				rxdown = rxdown + rshutdown.getLong(number8);
			}
			rxdown = rxdown / 1024 / 1024;
			mDown_total.setText(rxdown + "MB");
		}
		if (tst.moveToNext()) {
			int number9 = tst.getColumnIndex("traffic");
			txup = tst.getLong(number9);
			int number_id_ts = tst.getColumnIndex("_id");
			id_number_r = tst.getInt(number_id_ts);
			if (tov.moveToNext()) {
				int number_id_to = tst.getColumnIndex("_id");
				id_number_t = tst.getInt(number_id_to);
				if (id_number_r == id_number_t) {
				} else {
					int number10 = tov.getColumnIndex("traffic");
					txup = tov.getLong(number10) - txup;
				}
			}
			while (tshutdown.moveToNext()) {
				int number11 = tshutdown.getColumnIndex("traffic");
				txup = txup + tshutdown.getLong(number11);
			}
			txup = txup / 1024 / 1024;
			mUp_total.setText(txup + "MB");
		}
		alltotal = rxdown + txup;
		mliuliang_total.setText(alltotal + "MB");
	}

	/**
	 * 
	 * 方法说明：显示时间
	 * 
	 */
	private void selectdate(final TextView tex) {
		new MyDatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				String monthstr, daystr;
				monthOfYear = monthOfYear + 1;
				if (monthOfYear < 10) {
					monthstr = "" + 0 + monthOfYear;
				} else {
					monthstr = "" + monthOfYear;
				}
				if (dayOfMonth < 10) {
					daystr = "" + 0 + dayOfMonth;
				} else {
					daystr = "" + dayOfMonth;
				}
				tex.setText(year + "-" + monthstr + "-" + daystr);

			}
		}, mcalendar.get(Calendar.YEAR), mcalendar.get(Calendar.MONTH),
				mcalendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	/**
	 * 
	 * 方法说明：把数据插入到系统数据库中
	 * 
	 */
	private void datainsert() {
		long g3_down_total = TrafficStats.getMobileRxBytes(); // 获取通过Mobile连接收到的字节总数，这里Android123提示大家不包含WiFi
		long g3_up_total = TrafficStats.getMobileTxBytes(); // Mobile发送的总字节数
		long mrdown_total = TrafficStats.getTotalRxBytes(); // 获取总的接受字节数，包含Mobile和WiFi等
		long mtup_total = TrafficStats.getTotalTxBytes(); // 总的发送字节数，包含Mobile和WiFi等
		//检测wifi是否存在
		WifiManager wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
		ConnectivityManager connect=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=connect.getActiveNetworkInfo();
		if(info!=null){
			if(wifi.isWifiEnabled()){
				minsert.insertNow(mrdown_total, RX, RXT, NORMAL);
				minsert.insertNow(mtup_total, TX, TXT, NORMAL);
				minsert.insertNow(g3_down_total, RXG, RX3G, NORMAL);
				minsert.insertNow(g3_up_total, TXG, TX3G, NORMAL);
			}
			if(info.getType()==ConnectivityManager.TYPE_MOBILE){
				minsert.insertNow(g3_down_total, RXG, RX3G, NORMAL);
				minsert.insertNow(g3_up_total, TXG, TX3G, NORMAL);}
		}
	}

	private void showdata() {
		long grx = 0, gtx = 0, rx = 0, tx = 0;
		Cursor rcursor = minsert.selectNow(RXG);
		Cursor rdaycursor = minsert.selectday(RXG, SHUTDOWN);
		Cursor tcursor = minsert.selectNow(TXG);
		Cursor tdaycursor = minsert.selectday(TXG, SHUTDOWN);
		if (rcursor.moveToNext()) {
			int rnumbor = rcursor.getColumnIndex("traffic");
			grx = rcursor.getLong(rnumbor);
			while (rdaycursor.moveToNext()) {
				int rnumborday = rdaycursor.getColumnIndex("traffic");
				grx = grx + rdaycursor.getLong(rnumborday);
			}
			grx = grx / 1024 / 1024;
			mg_down_total.setText(grx + "MB");
		}
		if (tcursor.moveToNext()) {
			int tnumbor = tcursor.getColumnIndex("traffic");
			gtx = tcursor.getLong(tnumbor);
			while (tdaycursor.moveToNext()) {
				int tnumborday = tdaycursor.getColumnIndex("traffic");
				gtx = gtx + tdaycursor.getLong(tnumborday);
			}
			gtx = gtx / 1024 / 1024;
			mg_up_total.setText(gtx + "MB");
		}
		long g_total = grx + gtx;
		mg_total.setText(g_total + "MB");
		Cursor mrcursor = minsert.selectNow(RX);
		Cursor mrdaycursor = minsert.selectday(RX, SHUTDOWN);
		Cursor mtcursor = minsert.selectNow(TX);
		Cursor mtdaycursor = minsert.selectday(TX, SHUTDOWN);
		if (mrcursor.moveToNext()) {
			int numberRx = mrcursor.getColumnIndex("traffic");
			rx = mrcursor.getLong(numberRx);
			while (mrdaycursor.moveToNext()) {
				int numberRxDay = mrdaycursor.getColumnIndex("traffic");
				rx = rx + mrdaycursor.getLong(numberRxDay);
			}
			rx = rx / 1024 / 1024;
			mDown_total.setText(rx + "MB");
		}
		if (mtcursor.moveToNext()) {
			int numberTx = mtcursor.getColumnIndex("traffic");
			tx = mtcursor.getLong(numberTx);
			while (mtdaycursor.moveToNext()) {
				int numberTxDay = mtdaycursor.getColumnIndex("traffic");
				tx = tx + mtdaycursor.getLong(numberTxDay);
			}
			tx = tx / 1024 / 1024;
			mUp_total.setText(tx + "MB");
		}
		long z_total = rx + tx;
		mliuliang_total.setText(z_total + "MB");
		mrcursor.close();
		mrdaycursor.close();
		mtcursor.close();
		mtdaycursor.close();
		rcursor.close();
		rdaycursor.close();
		tcursor.close();
		tdaycursor.close();
	}

	private void alarmsave() {
		Cursor alcursor = minsert.selectNow(flag);
		if (alcursor.moveToNext()) {

		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 50);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Intent it = new Intent("SAVE_LIULIANG_EVERYDAY");
			PendingIntent peit = PendingIntent.getBroadcast(
					getApplicationContext(), 0, it, 0);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar
					.getTimeInMillis(), 86400 * 1000, peit);
			minsert.insertsign(flag, flagname);
			if (TrafficMonitoringActivity.isLog) {
				Log.i("traffic", "alarmmangerstart>>>>>>>>>>uppstart");
			}
		}

	}


}
