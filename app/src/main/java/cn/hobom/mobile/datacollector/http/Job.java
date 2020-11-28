package cn.hobom.mobile.datacollector.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public abstract class Job implements Runnable {

	private static final String TAG = "Job";

	private static final int RETRY_TIME = 3;
	abstract String getUrl();

	public boolean isHttpPost(){
		return true;
	}
	abstract void handleResponse(Object result);

	abstract HttpEntity getHttpEntity();

	@Override
	public void run() {
	
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 20000);
		HttpConnectionParams.setSoTimeout(client.getParams(), 20000);
	    HttpConnectionParams.setSocketBufferSize(client.getParams(), 8000);
		int time = 0;
		do {
			try {
				HttpResponse response = null;
				if(isHttpPost()){
					HttpPost httpPost = new HttpPost(getUrl());
					httpPost.setEntity(getHttpEntity());
					response = client.execute(httpPost);
				}else{
					HttpGet httpGet = new HttpGet(getUrl());
					response = client.execute(httpGet);
				}
				if (response.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(response.getEntity());
					Log.i(TAG,"the result is:"+result);
					handleResponse(result);
					return;
				} else {
					throw new HttpException("连接错误");
				}
			}catch (HttpException e) {
				e.printStackTrace();
				client.getConnectionManager().shutdown();
				
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				client.getConnectionManager().shutdown();
				
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();

			}
		} while (time < RETRY_TIME);

		handleResponse(null);
		
	}

}
