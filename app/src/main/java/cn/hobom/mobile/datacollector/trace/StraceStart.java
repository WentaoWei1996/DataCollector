package cn.hobom.mobile.datacollector.trace;

import android.os.Environment;

import java.io.File;

import cn.hobom.mobile.datacollector.util.ShellUtils;

import static cn.hobom.mobile.datacollector.trace.GetLocalTrace.GetPPID;
import static cn.hobom.mobile.datacollector.util.ChineseToPinyin.getPinyin;

/**
 * Created by WWT on 2019/9/1.
 */

public class StraceStart extends Thread {
    private String apkname;

    public StraceStart(String apkname){
        this.apkname = apkname;
    }


    public void run(){
        String PPID = GetPPID();
        ShellUtils.execCommand("mount -o remount ,rw /",true);
        String straceCommand = "strace -o " + "/data/data/cn.hobom.mobile.datacollector/"+ getPinyin(apkname) + ".txt -f -e trace=all -p " + PPID;//"strace -c -f -e trace=all -p "+PPID;
        ShellUtils.execCommand(straceCommand,true);
    }
}
