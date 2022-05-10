package com.example.deepname.VO;

import java.util.HashMap;
import java.util.List;

public class AbbreviationRecommendVO {
    private String param_name;
    private String method_name;
    private HashMap<String, Float> possible_recommends;
    private String param_location;


    public AbbreviationRecommendVO() {

    }

    public AbbreviationRecommendVO(String param_name, String method_name, HashMap<String, Float> possible_recommends, String param_location) {
        this.param_name = param_name;
        this.method_name = method_name;
        this.possible_recommends = possible_recommends;
        if (param_location == null) {
            this.param_location = "-1";
        } else {
            this.param_location = param_location;
        }
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

    public HashMap<String,Float> getPossible_recommends() {
        return possible_recommends;
    }

    public void setPossible_recommends(HashMap<String,Float> possible_recommends) {
        this.possible_recommends = possible_recommends;
    }

    public String getParam_location() {
        return param_location;
    }

    public void setParam_location(String param_location) {
        this.param_location = param_location;
    }
}
