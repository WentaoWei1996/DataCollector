package cn.hobom.mobile.datacollector;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by o on 2018-03-18.
 */

public class CollectorApplication extends Application {
    /**
     * 全局上下文环境
     */
    public static Context mContext;
    public static CollectorApplication mApp;
    public  Map<String,Boolean>uploadedMap;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        uploadedMap = new HashMap<String,Boolean>();
        mApp = this;
    }
    public void addUpload(String filepath){
        if (!uploadedMap.containsKey(filepath)){
            uploadedMap.put(filepath,true);
        }
    }

    public boolean uploaded(String filepath){
        return uploadedMap.get(filepath);
    }
}
