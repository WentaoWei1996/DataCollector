package cn.hobom.mobile.datacollector.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by o on 2017/9/5.
 */

public class AppInfo implements Serializable {
    private String appName;
    private Drawable image;
    private String packagename;
    private boolean isNew;
    private String apkpath;


    public AppInfo(String appName, Drawable image,String packagename,boolean isNew,String apkpath) {
        this.appName = appName;
        this.image = image;
        this.packagename = packagename;
        this.isNew = isNew;
        this.apkpath = apkpath;
    }
    public AppInfo(){

    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getApkpath() {
        return apkpath;
    }

    public void setApkpath(String apkpath) {
        this.apkpath = apkpath;
    }
}
