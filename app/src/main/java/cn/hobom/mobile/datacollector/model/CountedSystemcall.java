package cn.hobom.mobile.datacollector.model;

/**
 * Created by WWT on 2019/9/3.
 */

public class CountedSystemcall {
    private String Systemcall;
    private int num;
    private Double frequency;

    public CountedSystemcall(){

    }

    public CountedSystemcall(String Systemcall,int num){
        this.Systemcall = Systemcall;
        this.num = num;
    }

    public CountedSystemcall(String Systemcall,Double frequency){
        this.Systemcall = Systemcall;
        this.frequency = frequency;
    }

    public CountedSystemcall(String Systemcall,int num,Double frequency){
        this.Systemcall = Systemcall;
        this.num = num;
        this.frequency = frequency;
    }

    public String getSystemcall() {
        return Systemcall;
    }

    public void setSystemcall(String systemcall) {
        Systemcall = systemcall;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object obj) {
        CountedSystemcall countedSystemcall = (CountedSystemcall)obj;
        return this.Systemcall.equals(countedSystemcall.Systemcall);
    }

    @Override
    public String toString() {
        return this.Systemcall +" "+ this.num + " " + this.frequency;
    }
}

