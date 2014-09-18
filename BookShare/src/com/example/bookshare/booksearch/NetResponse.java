package com.example.bookshare.booksearch;

public class NetResponse {
	private int mCode;        //��Ӧ��
	private Object mMessage;  //��Ӧ��Ϣ
	
	public NetResponse(int code, Object message){
		mCode = code;
		mMessage = message;
	}
	
	public int getCode(){
		return mCode;
	}
	
	public void setCode(int code){
		mCode = code;
	}
	
	public Object getMessage(){
		return mMessage;
	}
	
	public void setMessage(Object message){
		mMessage = message;
	}
}
