package com.example.deepname.VO;

public class MethodNameRecommendVO {
    private String method_name;
    private String possible_recommend;

    public MethodNameRecommendVO() {

    }

    public MethodNameRecommendVO(String method_name, String possible_recommend) {
        this.method_name = method_name;
        this.possible_recommend = possible_recommend;
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
}
