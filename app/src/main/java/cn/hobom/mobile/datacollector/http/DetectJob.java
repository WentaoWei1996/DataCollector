package cn.hobom.mobile.datacollector.http;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
//import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hobom.mobile.datacollector.model.HashSystemCall;
import cn.hobom.mobile.datacollector.model.Msg;
import cn.hobom.mobile.datacollector.util.HashProvider.*;

import static cn.hobom.mobile.datacollector.util.ChineseToPinyin.getPinyin;

public class DetectJob extends Job {

    private static transient Charset defaultCharset = Charset.forName("UTF-8");

    private HashMethod hashMethod = HashMethod.MD5;
    private HashFunction hashFunction = HashMethod.MD5.getHashFunction();

    private String apkname;
    private JobListener callback;
    public DetectJob(String apkname){

        this.apkname = apkname;
    }
    public void setCallback(JobListener callback){
        this.callback  = callback;
    }
    @Override
    String getUrl() {
        return ServiceConfiguration.detectUrl();
    }

    @Override
    void handleResponse(Object result) {
        if(result!=null){
            if(callback!=null){
                callback.responseSucceed(result);
            }
        }else{
            if(callback!=null){
                callback.responseFailed("network error");
            }
        }
    }


    @Override
    HttpEntity getHttpEntity() {
        /*JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("filename", this.appname);
            return new StringEntity(jsonObject.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;*/

//        JSONObject json = new JSONObject();
        File file = new File("/data/data/cn.hobom.mobile.datacollector/differentdeep"+getPinyin(apkname)+".txt");
        try {
            List<String> trace = new ArrayList<String>();
            try {
                FileInputStream fis = new FileInputStream("/data/data/cn.hobom.mobile.datacollector/differentdeep"+getPinyin(apkname)+".txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                String line = null;
                while ((line = br.readLine()) != null){
                    trace.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Msg msg = Msg.sussess();
            String str = null;
            String hashSystemCall = "";
            Double frequency = 0.0;
            int[] hash;
            byte[] bytes;

            String[] trSplit;
            for (String tr : trace){
                trSplit = tr.split(" ");
                str = trSplit[0];
                frequency = Double.parseDouble(trSplit[2]);

                bytes = str.getBytes(defaultCharset);
                hash = hashFunction.hash(bytes,1437759,10);
                for (int i = 0; i < hash.length; i++) {
                    hashSystemCall = hashSystemCall + " " +hash[i];
                }

                hashSystemCall = hashSystemCall.substring(1,hashSystemCall.length());
                msg = msg.add(hashSystemCall,frequency);
                hashSystemCall="";
            }


            /*HashSystemCall hashSystemCall = new HashSystemCall("817968 1219598 350978 830330 326598 415482 542555 764482 252251 350335",1.0);

            List<HashSystemCall> list = new ArrayList<HashSystemCall>();
            list.add(hashSystemCall);
            list.add(hashSystemCall);

            Msg msg = Msg.sussess();
            msg = msg.add("729106 903966 353311 196047 1282484 406214 2560 1436661 1267646 1047112",0.1);
            msg = msg.add("1239231 1196889 911045 1176617 1239950 274401 866949 556323 61521 1400074",0.2);
            msg = msg.add("609682 390211 1413432 26352 141092 1055277 1281661 723777 389526 345345",0.1);
            msg = msg.add("1277017 5286 791420 130620 294876 112490 296652 1098328 1243539 826240",0.2);
            msg = msg.add("817968 1219598 350978 830330 326598 415482 542555 764482 252251 350335",0.6);
            msg = msg.add("667626 905613 1023396 26946 800999 1060886 442098 61636 889269 711476",0.45);
            msg = msg.add("749425 157922 635018 944212 1280014 669138 1405239 207817 24295 145932",0.3);
            msg = msg.add("467721 309494 1268963 195724 1063567 776963 283895 774254 222152 405015",0.7);*/

            JSONObject jsonObject = (JSONObject) JSON.toJSON(msg);


            return new StringEntity(jsonObject.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
