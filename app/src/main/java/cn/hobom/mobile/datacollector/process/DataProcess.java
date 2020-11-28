package cn.hobom.mobile.datacollector.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hobom.mobile.datacollector.model.CountedSystemcall;
import cn.hobom.mobile.datacollector.util.ShellUtils;

import static cn.hobom.mobile.datacollector.util.ChineseToPinyin.getPinyin;

/**
 * Created by WWT on 2019/9/2.
 */

public class DataProcess {
    private static String path;
    private static String apkname;
    private List<CountedSystemcall> trace = null;

    public DataProcess(){

    }

    public DataProcess(String path,String apkname){
        this.path = path;
        this.apkname = apkname;

        SystemcallFilter();
        GetDiffDeepSystemcall();
    }

    public List<CountedSystemcall> getTrace(){
        return trace;
    }

    public static void SystemcallFilter(){
        ShellUtils.execCommand("chmod 777 /data/data/cn.hobom.mobile.datacollector/"+getPinyin(apkname)+".txt",true);

        File systemcalldocument = new File("/data/data/cn.hobom.mobile.datacollector/processed"+getPinyin(apkname)+".txt");
        FileInputStream fis = null;
        try {
            systemcalldocument.createNewFile();
            PrintStream ps = new PrintStream(new FileOutputStream(systemcalldocument));

            fis = new FileInputStream(new File("/data/data/cn.hobom.mobile.datacollector/"+getPinyin(apkname)+".txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null){
                if(line.indexOf("---") < 0){
                    Pattern pattern = Pattern.compile("(.*?)\\(");
                    String [] arr = line.split("\\s+");
                    for(String ss : arr){
                        Matcher m=pattern.matcher(ss);
                        while (m.find()){
                            if(m.group(1).length()==0)
                                break;
                            if((m.group(1)).substring(0,1).equals("<")){
                                continue;
                            }
                            ps.println(m.group(1));
                        }
                    }
                }
            }
            ps.close();
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GetDiffDeepSystemcall(){
        List diffdeepsystecall = new ArrayList();
        List counteddiffdeepsystemcall;

        List processedsystemcall;
        List netandfilesystemcall;
        String processedsystemcallpath = "/data/data/cn.hobom.mobile.datacollector/processed"+getPinyin(apkname)+".txt";

        netandfilesystemcall = SetSystemcall();

        processedsystemcall = LoadProcessedSystemcall(processedsystemcallpath);

        for (int j = 1; j <= 13; j++) {
            diffdeepsystecall.addAll(DiffDeepSystemcall(j,processedsystemcall,netandfilesystemcall));
        }
        counteddiffdeepsystemcall = Count(diffdeepsystecall);

        trace = counteddiffdeepsystemcall;

        String counteddiffdeepsystemcallpath = "/data/data/cn.hobom.mobile.datacollector/differentdeep"+getPinyin(apkname)+".txt";
        SaveSystemcall(counteddiffdeepsystemcallpath,counteddiffdeepsystemcall);
    }

    public static List DiffDeepSystemcall(int deep, List processedsystemcall, List netandfilesystemcall){
        List systemcallseries = new ArrayList();

        String syscall;
        for(int i = 0;i<processedsystemcall.size();i++){
            if(netandfilesystemcall.contains(processedsystemcall.get(i))){
                syscall = (String)processedsystemcall.get(i);
                int j;
                for(j = i+1;j<=i+deep;j++){
                    if(j>processedsystemcall.size()-1){
                        break;
                    }
                    if(netandfilesystemcall.contains(processedsystemcall.get(j))){
                        syscall = syscall + "->" + processedsystemcall.get(j);
                    }else {
                        break;
                    }
                }
                if((j-i)==deep){
                    systemcallseries.add(syscall);
                }
            }
        }
        return systemcallseries;
    }

    public static List LoadProcessedSystemcall(String path){
        ShellUtils.execCommand("chmod 777 " + path,true);
        List systemcall = new ArrayList();
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine())!=null){
                systemcall.add(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return systemcall;
    }

    public static List Count(List diffdeepsystemcall){
        CountedSystemcall count = null;
        List countedsystemcall = new ArrayList();

        int num = 1;
        Double frequency = 0.0;
        int i;
        for(i = 0;i<diffdeepsystemcall.size();i++){
            count = new CountedSystemcall((String)(diffdeepsystemcall.get(i)),0,0.0);
            if(countedsystemcall.contains(count)){
                continue;
            }else {
                for(int j=i+1;j<diffdeepsystemcall.size();j++){
                    if((diffdeepsystemcall.get(i)).equals(diffdeepsystemcall.get(j))){
                        num++;
                    }
                }
            }
            frequency = (num*1.0/diffdeepsystemcall.size());
            count.setNum(num);
            count.setFrequency(frequency);
            num = 1;
            countedsystemcall.add(count);
        }
        return countedsystemcall;
    }

    public static void SaveSystemcall(String path,List counteddiffdeepsystemcall){

        File savedifferentdeepsystemcall = new File(path);

        try {
            savedifferentdeepsystemcall.createNewFile();
            PrintStream ps = new PrintStream(new FileOutputStream(savedifferentdeepsystemcall));

            for(int i=0;i<counteddiffdeepsystemcall.size();i++){
                ps.println(counteddiffdeepsystemcall.get(i));
            }
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List SetSystemcall(){
        List netandfilesystemcall = new ArrayList();

        netandfilesystemcall.add("fcntl");
        netandfilesystemcall.add("open");
        netandfilesystemcall.add("ctreat");
        netandfilesystemcall.add("close");
        netandfilesystemcall.add("pwrite");
        netandfilesystemcall.add("write");
        netandfilesystemcall.add("socketcall");
        netandfilesystemcall.add("socket");
        netandfilesystemcall.add("epoll_createl");
        netandfilesystemcall.add("bind");
        netandfilesystemcall.add("connect");
        netandfilesystemcall.add("accept");
        netandfilesystemcall.add("sendto");
        netandfilesystemcall.add("sendmsg");
        netandfilesystemcall.add("recv");
        netandfilesystemcall.add("recvfrom");
        netandfilesystemcall.add("recvmsg");
        netandfilesystemcall.add("openat");

        return netandfilesystemcall;
    }
}
