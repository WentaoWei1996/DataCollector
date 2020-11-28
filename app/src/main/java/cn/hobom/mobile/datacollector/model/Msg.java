package cn.hobom.mobile.datacollector.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WWT on 2019/8/28.
 */

public class Msg {
    private int code;
    private String msg;
    private Map<String,Double> extend = new HashMap<String, Double>();

    public Msg add(String key, Double value){
        this.getExtend().put(key,value);
        return this;
    }

    public static Msg sussess(){
        Msg result = new Msg();
        result.setCode(100);
        result.setMsg("处理成功");
        return result;
    }

    public static Msg fail(){
        Msg result = new Msg();
        result.setCode(200);
        result.setMsg("处理失败");
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Double> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Double> extend) {
        this.extend = extend;
    }
}
