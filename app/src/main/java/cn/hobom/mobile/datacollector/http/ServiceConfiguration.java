package cn.hobom.mobile.datacollector.http;

public class ServiceConfiguration {
	//需要替换为具体的IP或域名
	private static String host="172.20.10.2";
	private static int port = 8080;
	private static final String PROJECTNAME = "detection";

	private static String baseUrl(){
		return "http://"+host+":"+port+"/"+PROJECTNAME;
	}

	public static String uploadUrl(){
		return baseUrl()+"/detect";
	}

	public static String detectUrl(){
		return baseUrl()+"/detect";
	}

	public static String traceUrl(){
		return baseUrl()+"/service/trace";
	}

	public static String getTraceContentUrl(String url){
		return baseUrl()+url;
	}
}