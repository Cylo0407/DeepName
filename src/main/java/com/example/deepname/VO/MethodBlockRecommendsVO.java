package com.example.deepname.VO;

import java.util.ArrayList;

public class MethodBlockRecommendsVO {
    private String method_name;
    private MethodNameRecommendVO method_recommend_infos;
    private ArrayList<AbbreviationRecommendVO> param_recommend_infos;
    private ArrayList<AbbreviationRecommendVO> variable_recommend_infos;

    private MethodBlockRecommendsVO() {
        this.param_recommend_infos = new ArrayList<>();
        this.variable_recommend_infos = new ArrayList<>();
    }

    public MethodBlockRecommendsVO(String method_name) {
        this();
        this.method_name = method_name;
    }

    public MethodBlockRecommendsVO(String method_name, MethodNameRecommendVO method_recommend_infos) {
        this();
        this.method_name = method_name;
        this.method_recommend_infos = method_recommend_infos;
    }

    public MethodBlockRecommendsVO(String method_name, MethodNameRecommendVO method_recommend_infos, ArrayList<AbbreviationRecommendVO> param_recommend_infos, ArrayList<AbbreviationRecommendVO> variable_recommend_infos) {
        this.method_name = method_name;
        this.method_recommend_infos = method_recommend_infos;
        this.param_recommend_infos = param_recommend_infos;
        this.variable_recommend_infos = variable_recommend_infos;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public ArrayList<AbbreviationRecommendVO> getParam_recommend_infos() {
        return param_recommend_infos;
    }

    public void setParam_recommend_infos(ArrayList<AbbreviationRecommendVO> param_recommend_infos) {
        this.param_recommend_infos = param_recommend_infos;
    }

    public ArrayList<AbbreviationRecommendVO> getVariable_recommend_infos() {
        return variable_recommend_infos;
    }

    public void setVariable_recommend_infos(ArrayList<AbbreviationRecommendVO> variable_recommend_infos) {
        this.variable_recommend_infos = variable_recommend_infos;
    }

    public MethodNameRecommendVO getMethod_recommend_infos() {
        return method_recommend_infos;
    }

    public void setMethod_recommend_infos(MethodNameRecommendVO method_recommend_infos) {
        this.method_recommend_infos = method_recommend_infos;
    }

    public void addToParam_recommend_infos(AbbreviationRecommendVO param_recommend_info) {
        this.param_recommend_infos.add(param_recommend_info);
    }

    public void addToVariable_recommend_infos(AbbreviationRecommendVO variable_recommend_info) {
        this.variable_recommend_infos.add(variable_recommend_info);
    }
}
