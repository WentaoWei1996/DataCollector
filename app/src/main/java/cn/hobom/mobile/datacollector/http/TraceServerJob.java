package cn.hobom.mobile.datacollector.http;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by o on 2018-03-18.
 */

public class TraceServerJob extends Job {

    private String appname;
    private JobListener callback;
    public TraceServerJob(String appname){
        this.appname = appname;
    }
    public void setCallback(JobListener callback){
        this.callback  = callback;
    }
    @Override
    String getUrl() {
        return ServiceConfiguration.traceUrl();
    }

    @Override
    void handleResponse(Object result) {
        if(result!=null){
            if(callback!=null){
                callback.responseSucceed(result);
            }
        }else{
            if(callback!=null){
                callback.responseFailed("network error");
            }
        }
    }

    @Override
    HttpEntity getHttpEntity() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("filename", this.appname);
            return new StringEntity(jsonObject.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
