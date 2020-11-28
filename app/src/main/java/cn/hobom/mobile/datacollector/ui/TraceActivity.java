package cn.hobom.mobile.datacollector.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.hobom.mobile.datacollector.R;
import cn.hobom.mobile.datacollector.http.JobListener;
import cn.hobom.mobile.datacollector.http.JobManager;
import cn.hobom.mobile.datacollector.http.ServiceConfiguration;
import cn.hobom.mobile.datacollector.model.CountedSystemcall;
import cn.hobom.mobile.datacollector.process.DataProcess;
import cn.hobom.mobile.datacollector.util.HeaderUtil;
import cn.hobom.mobile.datacollector.util.ShellUtils;
import cn.hobom.mobile.datacollector.trace.StraceStart;

import static cn.hobom.mobile.datacollector.trace.GetLocalTrace.GetPID;
import static cn.hobom.mobile.datacollector.util.ChineseToPinyin.getPinyin;

/**
 * Created by o on 2018-03-18.
 */

public class TraceActivity extends Activity {
    private String apkpath;
    private String apkname;
    private String packagename;
    private TextView content;
    private ProgressDialog pd;
    private Button traceLocal;
    private Button traceServer;
    private Button load;
//    private Button startStrace;
//    private Button stopStrace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trace);
        apkpath = getIntent().getStringExtra("apkpath");
        apkname = getIntent().getStringExtra("apkname");
        packagename = getIntent().getStringExtra("packagename");
        setupView();
    }

    private void setupView(){
        HeaderUtil.show(this, true, false, true, false);
        HeaderUtil.initCenterTitle(this, "获取行为日志");

        traceLocal = (Button) findViewById(R.id.traceLocal);
        traceServer = (Button) findViewById(R.id.traceServer);
        load = (Button) findViewById(R.id.processandload);
        content = (TextView)findViewById(R.id.content);
//        startStrace = (Button) findViewById(R.id.startstrace);
//        stopStrace = (Button) findViewById(R.id.stopstrace);

        traceServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceServer();
            }
        });

        traceLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraceActivity.this,LocalTraceActivity.class);
                intent.putExtra("packagename",packagename);
                intent.putExtra("apkname",apkname);
                intent.putExtra("apkpath",apkpath);
                startActivity(intent);
//                traceLocal();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CountedSystemcall> trace;
                StringBuilder sb = new StringBuilder();

                File file = new File("/data/data/cn.hobom.mobile.datacollector/differentdeep"+getPinyin(apkname)+".txt");

                if (file.exists()){
                    Toast.makeText(TraceActivity.this, "日志加载中", Toast.LENGTH_LONG).show();

                    try {
                        FileInputStream fis = new FileInputStream("/data/data/cn.hobom.mobile.datacollector/differentdeep"+getPinyin(apkname)+".txt");
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);

                        String line = null;
                        while ((line = br.readLine()) != null){
                            sb.append(line + "\n");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(TraceActivity.this, "日志处理中，请稍后......", Toast.LENGTH_LONG).show();

                    DataProcess dataProcess = new DataProcess(apkpath,apkname);
                    trace = dataProcess.getTrace();
                    for (CountedSystemcall systemcall : trace){
                        sb.append(systemcall.toString() + "\n");
                    }
                }

                content.setText(sb.toString());
            }
        });

        /*startStrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StraceStart strace = new StraceStart(apkname);
                strace.start();

                *//*StringBuilder sb = new StringBuilder();

                String PPID = GetPPID();
                sb.append(PPID);

                ShellUtils.execCommand("mount -o remount ,rw /",true);

                String straceCommand = "strace -c -f -e trace=all -p "+PPID;//-T -ttt -f -e trace=all -p " + PPID;//"strace -o " + apkname + ".txt -T -ttt -f -e trace=all -p " + PPID;
                ShellUtils.CommandResult commandResult = ShellUtils.execCommand(straceCommand, true);
                sb.append(commandResult.successMsg);
                ShellUtils.CommandResult commandResult1 = ShellUtils.execCommand("ps", false);
                sb.append(commandResult1.successMsg);
                content.setText(sb.toString());*//*
            }
        });

        stopStrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stracePID = GetPID("strace");
//                ShellUtils.CommandResult commandResult = ShellUtils.execCommand("am force-stop strace", true);
                ShellUtils.execCommand("kill -9 " + stracePID, true);
//                content.setText("stracePID:"+stracePID + commandResult.successMsg);
            }
        });*/
    }

    private void traceLocal(){
        String str = JobManager.getInstance().traceLocal(apkpath,apkname,packagename);
//        pd = ProgressDialog.show(this,"提示",str,true,true);
        content.setText(str);
    }

    private void traceServer(){
        pd = ProgressDialog.show(this,"提示","获取日志中,请稍候...",true,true);
        JobManager.getInstance().traceServer(apkname, new JobListener() {
            @Override
            public void responseSucceed(Object result) {
                try {
                    JSONObject obj = new JSONObject((String) result);
                    if (obj.getBoolean("success")) {
                        final String url = obj.optString("result");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                showServerResult(url);
                            }
                        });
                    }else{
                        if(pd!=null){
                            pd.dismiss();
                        }
                        final String info = obj.optString("result");
                        final int state = obj.getInt("state");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (state == 0){
                                    showUploadDialog(info);
                                }else {
                                    Toast.makeText(TraceActivity.this, info, Toast.LENGTH_LONG).show();
                                }
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

    private  void showServerResult(String url){

        final String fullpath = ServiceConfiguration.getTraceContentUrl(url);
        Toast.makeText(this,fullpath,Toast.LENGTH_LONG).show();
        new Thread() {
            public void run() {
                final String result = getWebText(fullpath);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(pd!=null){
                            pd.dismiss();
                        }
                        content.setText(result);
                    }
                });
            }
        }.start();
    }

    public String getWebText(String remoteUrl) {
        try {
            /*
             * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
             * <uses-permission android:name="android.permission.INTERNET" />
             */
            URL url = new URL(remoteUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);
            // 取得inputStream，并进行读取
            InputStream input = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void showUploadDialog(String info){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示").setMessage(info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(TraceActivity.this,FileBrowser.class);
                        startActivityForResult(intent,200);
                    }
                })
                .setNegativeButton("取消",null);
        builder.create().show();
    }
    private void upload(){
        pd = ProgressDialog.show(this,"上传","上传中,请稍候...",true,true);
        JobManager.getInstance().upload(apkpath,apkname, new JobListener() {
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
                                Toast.makeText(TraceActivity.this,"上传成功，请稍候查看日志",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        final String info = obj.optString("result");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TraceActivity.this,info,Toast.LENGTH_LONG).show();
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
                upload();
            }
        }
    }
}
