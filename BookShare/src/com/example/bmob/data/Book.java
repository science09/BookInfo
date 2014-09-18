package com.example.bmob.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Book extends BmobObject{
	/**
	 * 数据库中的书表
	 */
	private static final long serialVersionUID = 8186054022391505464L;
	private String title;
	private BmobFile cover;
	private String author;
	private String publisher;
	private String publishDate;
	private String ISBN;
	private String desc;
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public BmobFile getCover(){
		return cover;
	}
	
	public void setCover(BmobFile cover){
		this.cover = cover;
	}
	
	public String getAuthor(){
		return author;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String getPublisher(){
		return publisher;
	}
	
	public void setPublisher(String mpublisher){
		this.publisher = mpublisher;
	}
	
	public String getPublishDate(){
		return publishDate;
	}
	
	public void setPublishDate(String publishDate){
		this.publishDate = publishDate;
	}
	
	public String getISBN(){
		return ISBN;
	}
	
	public void setISBN(String mISBN){
		this.ISBN = mISBN;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public void setDesc(String mdesc){
		this.desc = mdesc;
	}
}
