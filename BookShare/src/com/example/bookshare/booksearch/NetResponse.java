package com.example.bookshare.booksearch;

public class NetResponse {
	private int mCode;        //响应码
	private Object mMessage;  //响应信息
	
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
