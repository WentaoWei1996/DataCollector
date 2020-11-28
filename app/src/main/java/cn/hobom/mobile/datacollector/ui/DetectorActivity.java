package cn.hobom.mobile.datacollector.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.hobom.mobile.datacollector.R;
import cn.hobom.mobile.datacollector.http.JobListener;
import cn.hobom.mobile.datacollector.http.JobManager;
import cn.hobom.mobile.datacollector.util.HeaderUtil;

/**
 * Created by o on 2018-03-18.
 */

public class DetectorActivity extends Activity {

    private String apkpath;
    private String apkname;
    private String packagename;
    private Button detect;
    private TextView detectinfo;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detect);
        apkpath = getIntent().getStringExtra("apkpath");
        apkname = getIntent().getStringExtra("apkname");
        packagename = getIntent().getStringExtra("packagename");
        setupView();
    }

    private void setupView(){
        HeaderUtil.show(this, true, false, true, false);
        HeaderUtil.initCenterTitle(this, "检测");

        detect = (Button)findViewById(R.id.start);
        detectinfo = (TextView)findViewById(R.id.result);
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  detect();
            }
        });
    }

    private void detect(){
        pd = ProgressDialog.show(this,"检测","检测中,请稍候...",true,true);
        final long startTime = System.currentTimeMillis();
        JobManager.getInstance().detect(apkname, new JobListener() {
            @Override
            public void responseSucceed(Object result) {
                long endTime = System.currentTimeMillis();
                if (pd!=null){
                    pd.dismiss();
                }

                try {
                    JSONObject obj = new JSONObject((String) result);
                    JSONObject extend = obj.getJSONObject("extend");
                    if (obj.getInt("code")==100){
                        String str = extend.getString("result");
                        str = str + " \n检测耗时：" + (endTime - startTime)+"" + "ms";
                        final String info = str;

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                detectinfo.setText(info);
                            }
                        });
                    }else {
                        final String info = obj.optString("msg");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                detectinfo.setText("else");
                                showUploadDialog(info);
                            }
                        });
                    }

                    /*if (obj.getBoolean("success")) {
                        final String info = obj.optString("result");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                detectinfo.setText(info);
                            }
                        });
                    }else{
                        final String info = obj.optString("result");
                        final int state = obj.getInt("state");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (state == 0){
                                    showUploadDialog(info);
                                }else {
                                    Toast.makeText(DetectorActivity.this, info, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }*/
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void responseFailed(String reason) {

            }
        });
    }

    private void showUploadDialog(String info){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示").setMessage(info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DetectorActivity.this,FileBrowser.class);
                        startActivityForResult(intent,200);
                    }
                })
                .setNegativeButton("取消",null);
        builder.create().show();
    }
    private void upload(){
       pd = ProgressDialog.show(this,"上传","上传中,请稍候...",true,true);
        JobManager.getInstance().upload(apkpath, apkname,new JobListener() {
            @Override
            public void responseSucceed(Object result) {
                if (pd!=null){
                    pd.dismiss();
                }
                try {
                    JSONObject obj = new JSONObject((String) result);
                    if (obj.getBoolean("success")) {
                        final String info = obj.optString("result");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetectorActivity.this,"上传成功，请稍候检测",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        final String info = obj.optString("result");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetectorActivity.this,info,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void responseFailed(String reason) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200){
            if(resultCode==Activity.RESULT_OK){
                apkpath = data.getStringExtra("apkpath");
                Toast.makeText(this,apkpath,Toast.LENGTH_LONG).show();
                upload();
            }
        }
    }
}
