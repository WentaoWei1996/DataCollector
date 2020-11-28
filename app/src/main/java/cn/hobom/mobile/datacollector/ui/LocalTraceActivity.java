package cn.hobom.mobile.datacollector.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import cn.hobom.mobile.datacollector.R;
import cn.hobom.mobile.datacollector.http.JobManager;
import cn.hobom.mobile.datacollector.trace.StraceStart;
import cn.hobom.mobile.datacollector.util.HeaderUtil;
import cn.hobom.mobile.datacollector.util.ShellUtils;

import static cn.hobom.mobile.datacollector.trace.GetLocalTrace.GetPID;
import static cn.hobom.mobile.datacollector.util.ChineseToPinyin.getPinyin;

/**
 * Created by WWT on 2019/9/1.
 */

public class LocalTraceActivity extends Activity{
    private String apkpath;
    private String apkname;
    private String packagename;
    private Button startStrace;
    private Button monkey;
    private Button stopStrace;
    private Button load;
    private TextView content;
    StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_trace);
        apkpath = getIntent().getStringExtra("apkpath");
        apkname = getIntent().getStringExtra("apkname");
        packagename = getIntent().getStringExtra("packagename");
        setupView();
    }

    private void setupView(){
        HeaderUtil.show(this,true,false,true,false);
        HeaderUtil.initCenterTitle(this,"本地获取日志");
        startStrace = (Button) findViewById(R.id.startstrace);
        monkey = (Button) findViewById(R.id.monkey);
        stopStrace = (Button) findViewById(R.id.stopstrace);
        load = (Button) findViewById(R.id.load);
        content = (TextView) findViewById(R.id.localcontent);

        startStrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StraceStart strace = new StraceStart(apkname);
                strace.start();
            }
        });

        monkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = JobManager.getInstance().traceLocal(apkpath,apkname,packagename);
                sb.append(result);
            }
        });

        stopStrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stracePID = GetPID("strace");
                ShellUtils.execCommand("kill -9 " + stracePID, true);
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLocal();
            }
        });
    }

    private void loadLocal(){
        final StringBuilder sb = new StringBuilder();
//        List<String> result = null;
        String result=null;
        String path = "/data/data/cn.hobom.mobile.datacollector/"+getPinyin(apkname)+".txt";

//        verifyStoragePermissions(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((LocalTraceActivity) this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        }

        ShellUtils.execCommand("chmod 777 /data/data/cn.hobom.mobile.datacollector/"+getPinyin(apkname)+".txt",true);

        try {
            File file = new File(path);
            int length = (int) file.length();
            byte[] buff = new byte[length];
            FileInputStream fis = new FileInputStream(file);

            fis.read(buff);
            fis.close();
            result = new String(buff,"UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            sb.append(e);
            Toast.makeText(LocalTraceActivity.this,"没有找到指定文件",Toast.LENGTH_SHORT).show();
        }

        /*List<String> result = null;
        ReadFile readFile = new ReadFile(apkname);
        readFile.start();
        result = readFile.getReault();
        try {
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            if (is != null){
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                String line;
                while ((line = br.readLine()) != null){
                    result.add(line);
                }
                br.close();
                isr.close();
                is.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            result.add("Exception");
            result.add(e.toString());
        }*/

        content.setText(sb.toString()+"\n"+result);
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
