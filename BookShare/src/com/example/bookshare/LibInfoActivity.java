package com.example.bookshare;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bmob.data.Book;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class LibInfoActivity extends BaseActivity {
	private List<Map<String, Object>> liblist;
	private ListView  mlibBooks;
	private Button  addBtn;
	private List<Book>  mbooksList;
	private Handler handler = null;
	private ImageView imv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lib_info);
		Intent intent = this.getIntent();
		String libname = intent.getExtras().getString("libname");
		Log.d("LibInfoActivity", libname);
		this.setTitle(libname);
		
		mlibBooks = (ListView) findViewById(R.id.list_lib_book);
		imv  = (ImageView) findViewById(R.id.lib_image);
		
		//添加图书按钮
		addBtn = (Button) findViewById(R.id.btn_add);
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LibInfoActivity.this, AddBookActivity.class);
				startActivity(intent);
			}
		});
		
		//使用子线程加载数据
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					BmobQuery<Book> mBook = new BmobQuery<Book>();
					mBook.setLimit(1000); //最大限制
					mBook.findObjects(getBaseContext(), new FindListener<Book>() {
						
						@Override
						public void onSuccess(List<Book> list) {
							// TODO Auto-generated method stub
							mbooksList = list;
							handler.sendMessage(handler.obtainMessage());
						}
						
						@Override
						public void onError(int arg0, String msg) {
							// TODO Auto-generated method stub
							toast("获取数据失败！"+msg);
						}
					});
					//发送消息，把数据传递
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		
		try{
			//开线程接收数据
			new Thread(runnable).start();
			handler = new Handler(){
				public void handleMessage(Message msg){
					if(msg.what == 0){
						liblist = new  ArrayList<Map<String,Object>>(mbooksList.size());
						for(int i = 0; i < mbooksList.size(); i++){
							Map<String, Object> map = new HashMap<String, Object>();

							if(mbooksList.get(i).getCover() == null){
								map.put("book_img", R.drawable.android);
							}else{
								//imv = (ImageView) findViewById(R.id.book_img);	
								mbooksList.get(i).getCover().loadImage(getBaseContext(), imv);
								map.put("book_img", R.drawable.favicon);
								//map.put("book_img", imv.getDrawingCache(true).toString());
							}
							map.put("book_name", mbooksList.get(i).getTitle());
							map.put("book_desc", mbooksList.get(i).getAuthor());
							liblist.add(map);
						}
						
						SimpleAdapter mAdapter = new SimpleAdapter(LibInfoActivity.this, liblist, R.layout.item_row,
				    			new String[] {"book_img", "book_name", "book_desc"},
				    			new int[] {R.id.book_img, R.id.book_name, R.id.book_desc});
						
						mAdapter.setViewBinder(new ViewBinder() {
							
							@Override
							public boolean setViewValue(View view, Object data, String textRepresentation) {
								// TODO Auto-generated method stub
								if(view instanceof ImageView && data instanceof Drawable){
									ImageView iv = (ImageView) view;
									Bitmap bitmap = (Bitmap) data;
									//Bitmap b = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
									//iv.setImageBitmap(b);
									//iv.setImageDrawable((Drawable)data);
									iv.setImageBitmap((Bitmap) data);
									return true;
								}else{
									return false;
								}
							}
						});
						
						mlibBooks.setAdapter(mAdapter);
					}
				}
			};
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
	}

//	private void updateListView(){
//		BmobQuery<Book> mBook = new BmobQuery<Book>();
//		mBook.findObjects(getBaseContext(), new FindListener<Book>() {
//			@Override
//			public void onSuccess(List<Book> obj) {
//				// TODO Auto-generated method stub
//				toast("查询成功："+ obj.get(1).getTitle());
//				mbooksList = obj;
//			}
//			
//			@Override
//			public void onError(int arg0, String msg) {
//				// TODO Auto-generated method stub
//				toast("查询失败："+msg);
//			}
//		});
//	}
	
}
