package com.example.deepname.VO;

public class MethodNameRecommendVO {
    private String method_name;
    private String possible_recommend;
    private float distance;
    private Integer location;

    public MethodNameRecommendVO() {

    }

    public MethodNameRecommendVO(String method_name, String possible_recommend, float distance, Integer location) {
        this.method_name = method_name;
        this.possible_recommend = possible_recommend;
        this.distance = distance;
        this.location = location;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getPossible_recommend() {
        return possible_recommend;
    }

    public void setPossible_recommend(String possible_recommend) {
        this.possible_recommend = possible_recommend;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }
}
