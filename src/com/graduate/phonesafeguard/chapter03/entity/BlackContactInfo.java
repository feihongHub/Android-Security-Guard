package com.graduate.phonesafeguard.chapter03.entity;

public class BlackContactInfo {
	public String phoneNumber;//��ϵ�˵绰
	public String contactName;//��ϵ������
	/**
	 * ������ģʽ      1 �绰����   2��������   3�绰����������
	 */
	public int mode;//�������е�״̬
//	public int id;//������id
	//
	public String getModeString(int mode){
		switch (mode) {
		case 1:	
			return "�绰����";
		case 2:	
			return "��������";
		case 3:	
			return "�绰����������";

		}
		return "";
		
	}
	
}
