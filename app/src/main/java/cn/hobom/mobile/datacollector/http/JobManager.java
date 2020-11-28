package cn.hobom.mobile.datacollector.http;

import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hobom.mobile.datacollector.CollectorApplication;
import cn.hobom.mobile.datacollector.trace.GetLocalTrace;
import cn.hobom.mobile.datacollector.util.Connector;


public class JobManager {

	private static final String TAG = "JobManager";

	private final ExecutorService executor;

	private static JobManager instance;

	public static synchronized JobManager getInstance() {
		if (instance == null) {
			instance = new JobManager();
		}
		return instance;
	}

	private JobManager() {
		executor = Executors.newCachedThreadPool();
	}

	private void submitJob(Job request) {
		if (!Connector.isConnected(CollectorApplication.mApp)) {
			Toast.makeText(CollectorApplication.mApp, "网络错误",
					Toast.LENGTH_SHORT).show();
		} else {
			executor.execute(request);
		}
	}

	public void upload(String apkpath,String filename,JobListener listener){
		UploadJob loginJob = new UploadJob(apkpath,filename);
		loginJob.setCallback(listener);
		submitJob(loginJob);
	}

	public void detect(String appname, JobListener listener) {
		DetectJob loginJob = new DetectJob(appname);
		loginJob.setCallback(listener);
		submitJob(loginJob);
	}

	public void traceServer(String appname,JobListener listener){
		TraceServerJob loginJob = new TraceServerJob(appname);
		loginJob.setCallback(listener);
		submitJob(loginJob);
	}

	public String traceLocal(String apkpath,String apkname,String packagename){
		String flag = null;

		GetLocalTrace traceLocalJob = new GetLocalTrace(apkpath,apkname,packagename);

		return traceLocalJob.execShell();
	}

}
