package com.example.bookshare;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.example.bmob.data.RegUser;
import com.example.bmob.data.library;
import com.example.bookshare.util.CommonUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener{
	Context  mContext;
	EditText et_username, et_password;
	Button btn_login;
	TextView btn_register;
	int libNums = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		mContext = this.getApplicationContext();
		init();	
	}

	private void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (TextView) findViewById(R.id.btn_register);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		/* 通过XML文件获取之前登陆的用户名和密码 */
		SharedPreferences prefs = getSharedPreferences("loginfo", MODE_PRIVATE);
		String user = prefs.getString("user", null);
		String pass = prefs.getString("pass", null);
		if(user != null){
			et_username.setText(user);
			et_password.setText(pass);
			/* 设置光标的位置为用户名的最后一个字符 */
			et_username.setSelection(user.length());
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_register) {
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
		} else {
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if(!isNetConnected){
				ShowToast(R.string.network_tips);
				return;
			}
			
			BmobQuery<library> query = new BmobQuery<library>();
			query.findObjects(mContext, new FindListener<library>(){

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					toast("查询失败："+ arg1);
				}
				@Override
				public void onSuccess(List<library> arg0) {
					// TODO Auto-generated method stub
					//下面的代码实现的是保存数据
					SharedPreferences sharedpreferences = getSharedPreferences("libs", MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putInt("num", arg0.size());
					Set<String> libs = new HashSet<String>();
					//Set<String> desc = new HashSet<String>();
					for(int i = 0; i < arg0.size(); i++){
						Log.d("LogActivity", arg0.get(i).getName() + arg0.get(i).getDetails());
						libs.add(arg0.get(i).getName() + "=#@$@#+" + arg0.get(i).getDetails());
						//desc.add(arg0.get(i).getDetails());
					}
					editor.putStringSet("libs", libs);
//					editor.putStringSet("descript", desc);
					editor.commit();
				}		
			});
			
			login();
		}
	}

	
	private void login(){
		String username = et_username.getText().toString();
		String password = et_password.getText().toString();
		
		if (TextUtils.isEmpty(username)) {
			ShowToast(R.string.toast_error_username_null);
			return;
		}
		if (TextUtils.isEmpty(password)) {
			ShowToast(R.string.toast_error_password_null);
			return;
		}
		
		
		final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
		progress.setMessage("正在登陆...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		final RegUser regUser = new RegUser();
		regUser.setPassword(password);
		//regUser.setEmail(username);
		regUser.setUsername(username);
		regUser.login(LoginActivity.this,  new SaveListener(){
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.setMessage("正在获取好友列表...");					
												
					}					
				});
				/* 保存用户数据 */
				SharedPreferences prefs = getSharedPreferences("loginfo", MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				String username = et_username.getText().toString();
				String password = et_password.getText().toString();
				editor.putString("user", username);
				editor.putString("pass", password);
				editor.commit();
				
				progress.dismiss();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int errorcode, String arg0) {
				// TODO Auto-generated method stub
				progress.dismiss();	
				ShowToast(arg0);
			}
		});
	}
	
	
}
