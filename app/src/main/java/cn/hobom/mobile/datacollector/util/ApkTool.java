package cn.hobom.mobile.datacollector.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.hobom.mobile.datacollector.model.AppInfo;


/**
 * Created by o on 2017/9/5.
 */

public class ApkTool {

    static  String TAG = "ApkTool";
    public static List<AppInfo> mLocalInstallApps = null;

    public static List<AppInfo> scanLocalInstallAppList(Context context, PackageManager packageManager) {
        String installApps = PreferencesUtil.getSettingString("installApps","");
        List<String>installAppList = new LinkedList<>();
        if (!TextUtils.isEmpty(installApps)) {
            installAppList = com.alibaba.fastjson.JSONObject.parseArray(installApps, String.class);
        }
        List<String>newScanned = new LinkedList<>();
        List<AppInfo> myAppInfos = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
             //   过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                String appname = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                Log.i(TAG,"the appname is:"+appname);
                String apkpath = packageManager.getApplicationInfo(packageInfo.packageName, 0).sourceDir;

                AppInfo myAppInfo = new AppInfo();
                myAppInfo.setPackagename(packageInfo.packageName);
                myAppInfo.setAppName(appname);
                myAppInfo.setApkpath(apkpath);
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                if (!installAppList.contains(packageInfo.packageName)){
                    myAppInfo.setNew(true);
                    newScanned.add(packageInfo.packageName);
                }else{
                    myAppInfo.setNew(false);
                }
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
            installAppList.addAll(newScanned);


        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }

    /*
    * 获取程序 图标
    */
    public static Drawable getAppIcon(String packname,PackageManager pm){
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;
    }

    /*
    * 获取程序的名字
    */
    public static String getAppName(String packname,PackageManager pm){
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return null;
    }

}
