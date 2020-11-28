package cn.hobom.mobile.datacollector.http;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by o on 2018-03-31.
 */

public class UploadJob extends Job {
    private String filepath;
    private JobListener callback;
    private String filename;
    public UploadJob(String filepath,String filename){
        this.filepath = filepath;
        this.filename = filename;
    }
    public void setCallback(JobListener listener){
        this.callback = listener;
    }
    @Override
    String getUrl() {
        return ServiceConfiguration.uploadUrl();
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
        MultipartEntity entity = new MultipartEntity();
        File file = new File(filepath);
        entity.addPart("appname",filename);
        entity.addPart("apkname",file,true);
        return entity;
    }
}
