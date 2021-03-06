package com.graduate.phonesafeguard.chapter08.receiver;



import com.graduate.phonesafeguard.chapter08.TrafficMonitoringActivity;
import com.graduate.phonesafeguard.chapter08.db.DataSupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		DataSupport minsert = new DataSupport(context);
		// 获取通过Mobile连接收到的字节总数，这里Android123提示大家不包含WiFi
		long g3_down_total = TrafficStats.getMobileRxBytes();
		// Mobile发送的总字节数
		long g3_up_total = TrafficStats.getMobileTxBytes();
		// 获取总的接受字节数，包含Mobile和WiFi等
		long mrdown_total = TrafficStats.getTotalRxBytes();
		// 总的发送字节数，包含Mobile和WiFi等
		long mtup_total = TrafficStats.getTotalTxBytes();

		minsert.insertNow(g3_down_total, TrafficMonitoringActivity.RXG, TrafficMonitoringActivity.RX3G,
				TrafficMonitoringActivity.SHUTDOWN);
		minsert.insertNow(g3_up_total, TrafficMonitoringActivity.TXG, TrafficMonitoringActivity.TX3G,
				TrafficMonitoringActivity.SHUTDOWN);
		minsert.insertNow(mrdown_total, TrafficMonitoringActivity.RX, TrafficMonitoringActivity.RXT,
				TrafficMonitoringActivity.SHUTDOWN);
		minsert.insertNow(mtup_total, TrafficMonitoringActivity.TX, TrafficMonitoringActivity.TXT,
				TrafficMonitoringActivity.SHUTDOWN);
		
	}

}
