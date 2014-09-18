package com.example.bookshare.booksearch;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {
	private String mTitle = "";       //书名
	private Bitmap mCover;            //封面
	private String mAuthor = "";      //作者
	private String mPublisher = "";   //出版社
	private String mPublishDate = ""; //出版时间
	private String mISBN = "";        // ISBN
	private String mDesc = "";        // 内容介绍
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mTitle);
		dest.writeParcelable(mCover, flags);
		dest.writeString(mAuthor);
		dest.writeString(mPublisher);
		dest.writeString(mPublishDate);
		dest.writeString(mISBN);
		dest.writeString(mDesc);
	}
	
	public static final Parcelable.Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
		public BookInfo createFromParcel(Parcel source){
			BookInfo bookInfo = new BookInfo();
			bookInfo.mTitle = source.readString();
			bookInfo.mCover = source.readParcelable(Bitmap.class.getClassLoader());
			bookInfo.mAuthor = source.readString();
			bookInfo.mPublisher = source.readString();
			bookInfo.mPublishDate = source.readString();
			bookInfo.mISBN = source.readString();
			bookInfo.mDesc = source.readString();
			return bookInfo;
		}

		@Override
		public BookInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BookInfo[size];
		}
	};
	
	public String getTitle(){
		return mTitle;
	}
	
	public void setTitle(String title){
		mTitle = title;
	}
	
	public Bitmap getCover(){
		return mCover;
	}
	
	public void setCover(Bitmap cover){
		mCover = cover;
	}
	
	public String getAuthor(){
		return mAuthor;
	}
	
	public void setAuthor(String author){
		mAuthor = author;
	}
	
	public String getPublisher(){
		return mPublisher;
	}
	
	public void setPublisher(String publisher){
		mPublisher = publisher;
	}
	
	public String getPublishDate(){
		return mPublishDate;
	}
	
	public void setPublishDate(String publishDate){
		mPublishDate = publishDate;
	}
	
	public String getISBN(){
		return mISBN;
	}
	
	public void setISBN(String isbn){
		mISBN = "ISBN:" + isbn;
	}
	
	public String getDesc(){
		return mDesc;
	}
	
	public void setDesc(String desc){
		mDesc = desc;
	}
}
