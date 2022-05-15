package com.example.deepname.VO;

import java.util.ArrayList;

public class AbbreviationRecommendVO {
    private String param_name;
    private String method_name;
    private ArrayList<String> possible_recommends;
    private ArrayList<String> recommends_accuracy_type;
//    private ArrayList<Float> recommends_distance;
    private String param_location;


    public AbbreviationRecommendVO() {

    }

    public AbbreviationRecommendVO(String param_name, String method_name, ArrayList<String> possible_recommends, ArrayList<String> recommends_accuracy_type, String param_location) {
        this.param_name = param_name;
        this.method_name = method_name;
        this.possible_recommends = possible_recommends;
        this.recommends_accuracy_type = recommends_accuracy_type;
//        this.recommends_distance = recommends_distance;
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

    public String getParam_location() {
        return param_location;
    }

    public void setParam_location(String param_location) {
        this.param_location = param_location;
    }

    public ArrayList<String> getPossible_recommends() {
        return possible_recommends;
    }

    public void setPossible_recommends(ArrayList<String> possible_recommends) {
        this.possible_recommends = possible_recommends;
    }

    public ArrayList<String> getRecommends_accuracy_type() {
        return recommends_accuracy_type;
    }

    public void setRecommends_accuracy_type(ArrayList<String> recommends_accuracy_type) {
        this.recommends_accuracy_type = recommends_accuracy_type;
    }

//    public ArrayList<Float> getRecommends_distance() {
//        return recommends_distance;
//    }

//    public void setRecommends_distance(ArrayList<Float> recommends_distance) {
//        this.recommends_distance = recommends_distance;
//    }
}
