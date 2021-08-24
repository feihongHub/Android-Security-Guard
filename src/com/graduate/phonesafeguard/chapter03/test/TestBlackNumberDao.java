package com.graduate.phonesafeguard.chapter03.test;

import java.util.List;
import java.util.Random;

import com.graduate.phonesafeguard.chapter03.db.dao.BlackNumberDao;
import com.graduate.phonesafeguard.chapter03.entity.BlackContactInfo;

import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class TestBlackNumberDao extends AndroidTestCase{
	private Context context;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		context=getContext();
		super.setUp();
	}
	//������������
	public void testAdd(){
		BlackNumberDao dao=new BlackNumberDao(context);
		Random random=new Random(8979);
		for(long i=0;i<30;i++){
			BlackContactInfo info=new BlackContactInfo();
			info.phoneNumber=135000000+i+"";
			info.contactName="zhangsan"+i;
			info.mode=random.nextInt(3)+1;
			dao.add(info);
		}
	}
	//����ɾ������
	public void testDelete(){
		BlackNumberDao dao=new BlackNumberDao(context);
		
		for(long i=0;i<30;i++){
			BlackContactInfo info=new BlackContactInfo();
			info.phoneNumber=135000000+i+"";
			dao.delete(info);
		}
	}
	//���Է�ҳ��ѯ
	public void testGetPageBlackNumber(){
		BlackNumberDao dao=new BlackNumberDao(context);
		List<BlackContactInfo> list=dao.getPageBlackNumber(2, 5);
		for(int i=0;i<list.size();i++){
//			Log.i("TestBlackNumberDao", list.get(i).phoneNumber);
			System.out.println(list.get(i).phoneNumber);
		}
	}
	//���Ը��ݺ����ѯ����������
	public void testGetBlackContactMode(){
		BlackNumberDao dao=new BlackNumberDao(context);
		int mode = dao.getBlackContactMode(135000000+"");
		System.out.println(mode);
	}
	//��������Ŀ
	public void testGetTotalNumber(){
		BlackNumberDao dao=new BlackNumberDao(context);
		int total = dao.getTotalNumber();
		System.out.println(total);
	}
	//���Ժ����Ƿ������ݿ���
	public void testIsNumberExist(){
		BlackNumberDao dao=new BlackNumberDao(context);
		boolean isExist = dao.IsNumberExist(135000000+"");
		if(isExist){
			System.out.println("���ڸ���Ϣ");
		}else{
			System.out.println("δ�ҵ�����Ϣ");
		}
	}
}
