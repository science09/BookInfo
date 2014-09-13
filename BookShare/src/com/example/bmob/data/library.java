package com.example.bmob.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class library extends BmobObject{
	/**
	 * ͼ�����
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String details;
	private BmobFile image;
	private int size; /* ����ñ������ */
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getDetails(){
		return details;
	}
	
	public void setDetails(String details){
		this.details = details;
	}
	
	public BmobFile getPic(){
		return image;
	}
	
	public void setPic(BmobFile image){
		this.image = image;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size = size;
	}
}
