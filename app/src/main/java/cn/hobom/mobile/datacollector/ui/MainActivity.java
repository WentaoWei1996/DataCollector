package cn.hobom.mobile.datacollector.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import cn.hobom.mobile.datacollector.R;
import cn.hobom.mobile.datacollector.adapter.AppAdapter;
import cn.hobom.mobile.datacollector.model.AppInfo;
import cn.hobom.mobile.datacollector.util.ApkTool;
import cn.hobom.mobile.datacollector.util.HeaderUtil;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{
    private Handler mHandler;
    private ListView gridView;
    private AppAdapter mAppAdapter;
    private List<AppInfo>mAllApps = new LinkedList<AppInfo>();
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        setupView();
        loadApps();
    }
  private void setupView(){
      HeaderUtil.show(this, false, false, true, true);
      HeaderUtil.initCenterTitle(this, "恶意软件检测系统");
      HeaderUtil.initRightBtn(this, R.drawable.bigred, "设置", new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this,SettingActivity.class);
              startActivity(intent);
          }
      });
      gridView = (ListView)findViewById(R.id.list);
      mAppAdapter = new AppAdapter(this,mAllApps);
      gridView.setAdapter(mAppAdapter);
      check();
  }

    private void check(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "APP需要使用你的存储卡",
                    300, perms);
        }
            // Do not have permissions, request them now
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void loadApps(){

        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<AppInfo> appInfos = ApkTool.scanLocalInstallAppList(MainActivity.this,MainActivity.this.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        mAllApps.addAll(appInfos);
                        mAppAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
}
