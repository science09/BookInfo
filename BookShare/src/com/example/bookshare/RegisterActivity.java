package com.example.bookshare;

import com.example.bmob.data.RegUser;

import cn.bmob.v3.listener.SaveListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends BaseActivity {
	Button btn_register;
	EditText et_RegEmail, et_password, et_password_chk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		et_RegEmail = (EditText) findViewById(R.id.et_regemail);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_chk = (EditText) findViewById(R.id.et_password_again);
		
		btn_register = (Button) findViewById(R.id.btn_register);	
		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				register();
			}
		});
	}
	
	private static boolean checkEmail(String email){
		//验证邮箱的正则表达式
		String format = "\\p{Alpha}\\w{2,15}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
		if(email.matches(format)){
			return true;
		}else{
			return false;
		}
	}
	
	private void register(){
		String regEmail = et_RegEmail.getText().toString();
		String password = et_password.getText().toString();
		String pwd_again = et_password_chk.getText().toString();		
		
		if (TextUtils.isEmpty(regEmail)) {
			ShowToast(R.string.toast_error_username_null);
			return;
		}else if(!checkEmail(regEmail)){
			ShowToast(R.string.toast_error_emailformat_error);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ShowToast(R.string.toast_error_password_null);
			return;
		}
		if (!pwd_again.equals(password)) {
			ShowToast(R.string.toast_error_comfirm_password);
			return;
		}
		
		final ProgressDialog mProgress = new ProgressDialog(RegisterActivity.this);
		mProgress.setMessage("正在注册...");
		mProgress.setCanceledOnTouchOutside(false);
		mProgress.show();
			
		final RegUser regUser = new RegUser();
		regUser.setEmail(regEmail);
		regUser.setUsername(regEmail);
		regUser.setPassword(password);
		regUser.signUp(RegisterActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				mProgress.dismiss();
				ShowToast("注册成功");
				// 启动主页
				Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				//BmobLog.i(arg1);
				ShowToast("注册失败:" + arg1);
				mProgress.dismiss();
			}
		});
	}
}
