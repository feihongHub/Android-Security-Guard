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
		// ��ȡͨ��Mobile�����յ����ֽ�����������Android123��ʾ��Ҳ�����WiFi
		long g3_down_total = TrafficStats.getMobileRxBytes();
		// Mobile���͵����ֽ���
		long g3_up_total = TrafficStats.getMobileTxBytes();
		// ��ȡ�ܵĽ����ֽ���������Mobile��WiFi��
		long mrdown_total = TrafficStats.getTotalRxBytes();
		// �ܵķ����ֽ���������Mobile��WiFi��
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