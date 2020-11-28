package cn.hobom.mobile.datacollector.model;

/**
 * Created by WWT on 2019/8/28.
 */

public class HashSystemCall {
    private String hash;
    private Double averageFrequency;

    public HashSystemCall() {
    }

    public HashSystemCall(String hash, Double averageFrequency) {
        this.hash = hash;
        this.averageFrequency = averageFrequency;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Double getAverageFrequency() {
        return averageFrequency;
    }

    public void setAverageFrequency(Double averageFrequency) {
        this.averageFrequency = averageFrequency;
    }

    @Override
    public String toString() {
        return "HashSystemCall{" +
                "hash='" + hash + '\'' +
                ", averageFrequency=" + averageFrequency +
                '}';
    }
}
