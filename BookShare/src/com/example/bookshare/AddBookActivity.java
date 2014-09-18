package com.example.bookshare;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.example.bmob.data.Book;
import com.example.bookshare.booksearch.BookAPI;
import com.example.bookshare.booksearch.BookInfo;
import com.example.bookshare.booksearch.NetResponse;
import com.example.bookshare.util.CommonUtils;
import com.google.zxing.integration.IntentIntegrator;
import com.google.zxing.integration.IntentResult;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddBookActivity extends BaseActivity{
	private EditText ed_bookName;
	private EditText ed_bookAuthor;
	private EditText ed_bookISBN;
	private EditText ed_bookDesc;
	private Button btnBookSearch;
	private Button btnBookSave;
	private BmobFile mPic;
	private String imagPath;
	private ProgressDialog mProgressDialog;
	private DownloadHandler mHandler = new DownloadHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lib_add_book);
		setTitle("图书信息");
		initView();
		
		//扫一扫控件
		btnBookSearch.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				IntentIntegrator integrator = new IntentIntegrator(AddBookActivity.this);
				integrator.initiateScan();
			}
		});
		//保存数据
		btnBookSave.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO 保存数据到数据库
				if((ed_bookName.getText().toString().length() == 0) ||
				   (ed_bookAuthor.getText().toString().length() == 0) ||
				   (ed_bookISBN.getText().toString().length() == 0) ||
				   (ed_bookDesc.getText().toString().length() == 0)){
					toast("上述内容不能为空！");
					return ;
				}
				else{
					final Book mbook = new Book();
					//final BmobFile bookCover = new BmobFile(new File(imagPath));
					mbook.setTitle(ed_bookName.getText().toString());
					mbook.setAuthor(ed_bookAuthor.getText().toString());
					mbook.setISBN(ed_bookISBN.getText().toString());
					mbook.setDesc(ed_bookDesc.getText().toString());
					//mbook.setCover(bookCover);
					//mbook.save(getBaseContext());
//					mbook.save(getBaseContext(), new SaveListener() {
//						
//						@Override
//						public void onSuccess() {
//							// TODO Auto-generated method stub
//							toast("添加数据成功，返回objectId为：" + mbook.getObjectId());
//						}
//						
//						@Override
//						public void onFailure(int arg0, String arg1) {
//							// TODO Auto-generated method stub
//							toast("数据存储失败!");
//						}
//					});
				    
					final BmobFile bookCover = new BmobFile(new File(imagPath));
					bookCover.upload(getBaseContext(), new UploadFileListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							//Book book = new Book();
							//book.setCover(bookCover);
							mbook.setCover(bookCover);
							mbook.save(getBaseContext());
							ShowToast("图片上传成功!");
						}
						
						@Override
						public void onProgress(Integer arg0) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							toast("数据保存失败!");
						}
					});
				    
				}
				
			}
		});
	}

	private void initView(){
		ed_bookName = (EditText) findViewById(R.id.book_name);
		ed_bookAuthor = (EditText) findViewById(R.id.book_auther);
		ed_bookISBN = (EditText) findViewById(R.id.book_ISBN);
		ed_bookDesc = (EditText) findViewById(R.id.book_desc);
		btnBookSearch = (Button) findViewById(R.id.btn_booksearch);
		btnBookSave = (Button) findViewById(R.id.btn_booksave);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if((scanResult == null) || (scanResult.getContents() == null)){
			Log.d("ScanActivity", "User cancel scan by pressing back hardkey");
			return ;
		}
		
		//因为下载需要耗时，为了更好的用户体验,显示进度条进行提示
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("数据加载中...");
		mProgressDialog.show();
		// 启动下载线程
        DownloadThread thread = new DownloadThread(BookAPI.URL_ISBN_BASE + scanResult.getContents());
        thread.start();
	}
	
	/**
     * 启动图书详细信息显示界面，并将要显示的图书信息通过Intent传给即将启动的BookInfoDetailActivity
     */
    private void startBookInfoDetailActivity(BookInfo bookInfo) {
        if (bookInfo == null) {
            return;
        }
        
        ed_bookName.setText(bookInfo.getTitle());
        ed_bookAuthor.setText(bookInfo.getAuthor());
        ed_bookISBN.setText(bookInfo.getISBN().substring("ISBN:".length()));
        ed_bookDesc.setText(bookInfo.getDesc());
        ed_bookName.setCursorVisible(false); //把光标取消掉，默认在第一个edit编辑框显示光标
        
        File cache = new File(Environment.getExternalStorageDirectory(), "cache");
        if(!cache.exists()){
        	cache.mkdirs();
        }
        
        imagPath = cache + "/"+ bookInfo.getTitle() + ".jpg";
        Log.d("Dir", Environment.getExternalStorageDirectory().toString() + "==" +imagPath);
        File pic = new File(imagPath);
        
        try{
        	BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pic));
        	bookInfo.getCover().compress(Bitmap.CompressFormat.JPEG, 100, bos);
        	bos.flush();
        	bos.flush();
        	mPic = new BmobFile(new File(imagPath));
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
    }
	
    private static class DownloadHandler extends Handler {
        
        private AddBookActivity mActivity;
        
        public DownloadHandler(AddBookActivity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if ((msg.obj == null) || (mActivity.mProgressDialog == null) || (!mActivity.mProgressDialog.isShowing())) {
                return;
            }
            
            mActivity.mProgressDialog.dismiss();
            
            NetResponse response = (NetResponse) msg.obj;
            
            if (response.getCode() != BookAPI.RESPONSE_CODE_SUCCEED) {
                // 通信异常处理
                Toast.makeText(mActivity, "[" + response.getCode() + "]: "
                                                + mActivity.getString((Integer) response.getMessage()), 
                                                Toast.LENGTH_LONG).show();
            } else {
                // 通信正常时，迁移到书本显示界面
                mActivity.startBookInfoDetailActivity((BookInfo) response.getMessage());  	
            }
        }
        
    }
	
	private class DownloadThread extends Thread {
        
        private String mURL;
        
        public DownloadThread(String url) {
            super();
            mURL = url;
        }
        
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.obj = CommonUtils.download(mURL);
            mHandler.sendMessage(msg);
        }
    }
	
}
