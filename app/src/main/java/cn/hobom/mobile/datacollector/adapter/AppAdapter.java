package cn.hobom.mobile.datacollector.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.hobom.mobile.datacollector.model.AppInfo;
import cn.hobom.mobile.datacollector.ui.DetectorActivity;
import cn.hobom.mobile.datacollector.ui.TraceActivity;
import cn.hobom.mobile.datacollector.R;

/**
 * Created by o on 2017/9/5.
 */

public class AppAdapter extends BaseAdapter {
    private List<AppInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public AppAdapter(Context context, List<AppInfo>list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        GameHolder holder;
        final AppInfo result = list.get(i);
        if (convertView == null) {
            holder = new GameHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.app_item, null);
            holder.gameicon = (ImageView) convertView.findViewById(R.id.gameicon);
            holder.gamename = (TextView)convertView.findViewById(R.id.gamename);
            holder.trace = (Button)convertView.findViewById(R.id.trace);
            holder.detect = (Button)convertView.findViewById(R.id.detect);
            convertView.setTag(holder);
        } else {
            holder = (GameHolder) convertView.getTag();
        }

        holder.gamename.setText(result.getAppName());
        holder.gameicon.setImageDrawable(result.getImage());
        holder.trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TraceActivity.class);
                intent.putExtra("apkpath",result.getApkpath());
                intent.putExtra("apkname",result.getAppName());
                intent.putExtra("packagename",result.getPackagename());
                context.startActivity(intent);
            }
        });
        holder.detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetectorActivity.class);
                intent.putExtra("apkpath",result.getApkpath());
                intent.putExtra("apkname",result.getAppName());
                intent.putExtra("packagename",result.getPackagename());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class GameHolder{

        public ImageView gameicon;
        public TextView gamename;
        public Button trace;
        public Button detect;
    }
}


