package com.graduate.phonesafeguard.chapter07.entity;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	public String appName;
	public Drawable appIcon;
	public String packageName;
	public long appMemory;
	//用来标记app是否被选中
	public boolean isChecked;
	public boolean isUserApp;
}
