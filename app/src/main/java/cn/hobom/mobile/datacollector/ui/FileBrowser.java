package cn.hobom.mobile.datacollector.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.hobom.mobile.datacollector.R;
import cn.hobom.mobile.datacollector.util.HeaderUtil;


/**
 * Created by wsy on 17/7/8.
 */

public class FileBrowser extends Activity {
    public static final String TAG = FileBrowser.class.getSimpleName();
    ListView list;
    private String chooseApkPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filelist);
        setupView();
    }

   public void setupView(){
        list = (ListView) findViewById(R.id.list);
         HeaderUtil.show(this,true,false,true,false);
        HeaderUtil.initCenterTitle(this,"APK文件浏览器");
        readFiles();
        list.setAdapter(adapter);
        list.setOnItemClickListener(onItemClickListener);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String path = files.get(position).getAbsolutePath();
            chooseApkPath = path;
            Intent intent = getIntent();
            intent.putExtra("apkpath",chooseApkPath);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String path = files.get(position).getAbsolutePath();
            chooseApkPath = path;
            Intent intent = getIntent();
            intent.putExtra("apkpath",chooseApkPath);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private List<File> files = new ArrayList<>();
    private String getSDPath() {
        String sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
        }
        return sdDir ;
    }
    private void readFiles() {
            String dir = getSDPath();
            File dirF = new File(dir);
            if (!dirF.exists()) dirF.mkdirs();
            if (dirF.listFiles() != null) {
                for (File file : dirF.listFiles()) {
                    if (file.getName().endsWith(".apk"))
                        files.add(file);
                }
                adapter.notifyDataSetChanged();
            }
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public File getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(FileBrowser.this);
            textView.setTextSize(24);
            textView.setText(getItem(position).getName());
            return textView;
        }
    };
}
