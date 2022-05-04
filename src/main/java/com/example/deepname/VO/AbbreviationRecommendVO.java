package com.example.deepname.VO;

import java.util.List;

public class AbbreviationRecommendVO {
    private String param_name;
    private String method_name;
    private List<String> possible_recommends;

    public AbbreviationRecommendVO() {

    }

    public AbbreviationRecommendVO(String param_name, String method_name, List<String> possible_recommends) {
        this.param_name = param_name;
        this.method_name = method_name;
        this.possible_recommends = possible_recommends;
    }

    public String getParam_name() {
        return param_name;
    }

    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public List<String> getPossible_recommends() {
        return possible_recommends;
    }

    public void setPossible_recommends(List<String> possible_recommends) {
        this.possible_recommends = possible_recommends;
    }

}
