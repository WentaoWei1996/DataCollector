package cn.hobom.mobile.datacollector.http;

public interface JobListener {
	public void responseSucceed(Object result);
	public void responseFailed(String reason);
}
