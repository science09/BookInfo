package com.example.bmob.data;

import cn.bmob.v3.BmobUser;

public class RegUser extends BmobUser {
	/**
	 * ����̳�BmobUser����չ����������
	 */
	private static final long serialVersionUID = 1L;
	private String  QQ;
	private String  weibo;
	
	public void setQQ(String QQ){
		this.QQ = QQ;
	}
	
	public String getQQ(){
		return QQ;
	}
	
	public void setWeibo(String weibo){
		this.weibo = weibo;
	}
	
	public String getWeibo(){
		return weibo;
	}
}
