package com.example.deepname.VO;

import java.util.ArrayList;

public class MethodNameRecommendVO {
    private String method_name;
    private String method_signature;
    private String possible_recommend;
    private String accuracy_type;
    private String method_location;
    private ArrayList<String> method_params;

    public MethodNameRecommendVO() {

    }

    public MethodNameRecommendVO(String method_name, String method_signature, String possible_recommend, String accuracy_type, String method_location, ArrayList<String> method_params) {
        this.method_name = method_name;
        this.method_signature = method_signature;
        this.possible_recommend = possible_recommend;
        this.accuracy_type = accuracy_type;
        this.method_location = method_location;
        this.method_params = method_params;
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

    public String getAccuracy_type() {
        return accuracy_type;
    }

    public void setAccuracy_type(String accuracy_type) {
        this.accuracy_type = accuracy_type;
    }

    public String getMethod_location() {
        return method_location;
    }

    public void setMethod_location(String method_location) {
        this.method_location = method_location;
    }

    public String getMethod_signature() {
        return method_signature;
    }

    public void setMethod_signature(String method_signature) {
        this.method_signature = method_signature;
    }

    public ArrayList<String> getMethod_params() {
        return method_params;
    }

    public void setMethod_params(ArrayList<String> method_params) {
        this.method_params = method_params;
    }
}
