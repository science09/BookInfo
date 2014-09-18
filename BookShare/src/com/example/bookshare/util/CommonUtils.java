package com.example.bookshare.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.bookshare.R;
import com.example.bookshare.booksearch.BookAPI;
import com.example.bookshare.booksearch.BookInfo;
import com.example.bookshare.booksearch.NetResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CommonUtils {
	public static final String TAG = "CommonUtils";
	
	private static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
	
	/** ����Ƿ������� */
	public static boolean isNetworkAvailable(Context context){
		NetworkInfo netinfo = getNetworkInfo(context);
		if(netinfo != null){
			return netinfo.isAvailable();
		}else{
			return false;
		}
	}
	
	/** ����Ƿ���WIFI */
	public static boolean isWifi(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}
	
	/** ����Ƿ����ƶ����� */
	public static boolean isMobile(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}
	
	/** ���SD���Ƿ���� */
	public static boolean checkSdCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
     * �Ӷ����������ݣ����õ�ͼ������
     */
	public static NetResponse download(String url) {
        Log.v(TAG, "download from: " + url);
        
        NetResponse ret = downloadFromDouban(url);
        
        JSONObject message = null;
        
        try {
            message = new JSONObject(String.valueOf(ret.getMessage()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        switch (ret.getCode()) {
        case BookAPI.RESPONSE_CODE_SUCCEED:
            // �������ݣ�����BookInfo����
            ret.setMessage(parseBookInfo(message));
            break;

        default:
            // �쳣���ݣ����ش���ԭ��
            int errorCode = parseErrorCode(message); 
            ret.setCode(errorCode);
            ret.setMessage(getErrorMessage(errorCode));
            break;
        }
        
        return ret;
    }
	
	/**
     * �Ӷ��귵�صĴ�����Ϣ�н�����������
     */
    private static int parseErrorCode(JSONObject json) {
        int ret = BookAPI.RESPONSE_CODE_ERROR_NET_EXCEPTION;
        
        if (json == null) {
            return ret;
        }
        
        try {
            ret = json.getInt(BookAPI.TAG_ERROR_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return ret;
    }
    
    private static NetResponse downloadFromDouban(String url) {
        NetResponse ret = new NetResponse(BookAPI.RESPONSE_CODE_ERROR_NET_EXCEPTION, null);
        
        BufferedReader reader = null;
        HttpClient client = null;
        HttpResponse response = null;
        
        BasicHttpParams httpParams = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        client = new DefaultHttpClient(cm, httpParams);

        try {
            response = client.execute(new HttpGet(url));
            ret.setCode(response.getStatusLine().getStatusCode());
            
            StringBuffer sb = new StringBuffer();
            String line;
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            
            while ((line = reader.readLine()) != null) {
            	sb.append(line);
            }
            
            Log.v(TAG, "The content of Book: " + sb.toString());
            ret.setMessage(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
            
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return ret;
    }
    /**
     * ��JSONArray����������ض���ʽ���ַ��������磺
     *      ["string0", "string1"] -> "string0 string1"  
     */
    private static String parseJSONArray2String(JSONArray json, String split) {
        if ((json == null) || (json.length() < 1)) {
            return null;
        }
        
        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < json.length(); i++) {
            try {
            	sb = sb.append(json.getString(i));
            } catch (JSONException e) {
            	e.printStackTrace();
            }
            
            sb = sb.append(split);
        }
        
        sb.deleteCharAt(sb.length() - 1);
        
        Log.v(TAG, "parseJSONArray2String(" + json.toString() + "): " + sb.toString());
        return sb.toString();
    }

    /**
     * ���Ӷ����������õ��鼮JSON�ļ�������ͼ�����
     */
    private static BookInfo parseBookInfo(JSONObject json) {
        if (json == null) {
        	return null;
        }
        
        BookInfo bookInfo = null;
        
        try {
            bookInfo = new BookInfo();
            
            bookInfo.setTitle(json.getString(BookAPI.TAG_TITLE));
            bookInfo.setCover(downloadBitmap((json.getString(BookAPI.TAG_COVER))));
            bookInfo.setAuthor(parseJSONArray2String(json.getJSONArray(BookAPI.TAG_AUTHOR), " "));
            bookInfo.setPublisher(json.getString(BookAPI.TAG_PUBLISHER));
            bookInfo.setPublishDate(json.getString(BookAPI.TAG_PUBLISH_DATE));
            bookInfo.setISBN(json.getString(BookAPI.TAG_ISBN));
            bookInfo.setDesc(json.getString(BookAPI.TAG_SUMMARY).replace("\n", "\n\n"));
        } catch (JSONException e) {
        	e.printStackTrace();
        }

        return bookInfo;
    }
    
    /**
     * ���ݴ������ҵ���Ӧ���������ַ����ı��
     */
    private static int getErrorMessage(int errorCode) {
        int ret = R.string.error_message_default;
        
        switch (errorCode) {
        case BookAPI.RESPONSE_CODE_ERROR_NET_EXCEPTION:
            ret = R.string.error_message_net_exception;
            break;
            
        case BookAPI.RESPONSE_CODE_ERROR_BOOK_NOT_FOUND:
            ret = R.string.error_message_book_not_found;
            break;
            
        default:
            break;
        }
        
        return ret;
    }

    /**
     * ͨ��URL���ط���ͼƬ
     */
    private static Bitmap downloadBitmap(String url) {
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        Bitmap bm = null;
        
        try {
            conn = (HttpURLConnection) (new URL(url)).openConnection();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
 
        return bm;
    }
}
