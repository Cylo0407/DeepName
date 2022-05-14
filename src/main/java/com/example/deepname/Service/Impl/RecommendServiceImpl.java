package com.example.deepname.Service.Impl;

import abbrivatiate_expander.src.Step2.HandleCSV;
import com.example.deepname.Service.RecommendService;
import com.example.deepname.Utils.*;
import com.example.deepname.VO.AbbreviationRecommendVO;
import com.example.deepname.VO.MethodBlockRecommendsVO;
import com.example.deepname.VO.MethodNameRecommendVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;

@Service
@Transactional
public class RecommendServiceImpl implements RecommendService {

    @Override
    public MyResponse getPyService(String filepath) {
        return MyResponse.buildSuccess(getMethodNameRecommends(filepath));
    }

    private ArrayList<MethodNameRecommendVO> getMethodNameRecommends(String filepath) {
        String filename = filepath.substring(filepath.lastIndexOf('/') + 1);
        String prename = filename.substring(0, filename.indexOf('.'));

        String input_file_name = "D:/GTNM/deepname/temp/" + prename + '/' + prename + "_all.pkl";
        String output_file_name = "D:/GTNM/deepname/temp/" + prename + '/' + prename;
        File outputFile = new File("D:/GTNM/deepname/res/" + prename + '/' + prename + ".txt");

        if (!outputFile.exists()) {
            PythonRunner.step1(filepath, prename);
            PythonRunner.step2(prename);
            PythonRunner.step3(input_file_name, output_file_name);
            PythonRunner.step4(prename);
            PythonRunner.step_test(prename);
        }

        ArrayList<MethodNameRecommendVO> resList = new ArrayList<MethodNameRecommendVO>();
        try {
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));
            String line = "";
            File methodNames = new File(Global.TempPath + prename + '/' + prename + ".txt");

            BufferedReader signatureReader = new BufferedReader(new InputStreamReader(new FileInputStream(methodNames)));
            ArrayList<String> signatures = new ArrayList<String>();
            String signature = signatureReader.readLine();
            while (signature != null) {
                signatures.add(signature);
                signature = signatureReader.readLine();
            }

            int idx = 0;
            while ((line = outputReader.readLine()) != null) {
                String[] names = line.split(",");
                float distance = Levenshtein.getSimilarity(names[0], names[1]);
                String location = utils.getLocation(prename, names[0], signatures.get(idx));
                resList.add(new MethodNameRecommendVO(names[0], names[1], distance, location));
                idx++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resList;
    }

    @Override
    public MyResponse getParamExpand(String filepath) {
        ArrayList<ArrayList<AbbreviationRecommendVO>> recommendVOS;
        try {
            recommendVOS = HandleCSV.recommendProcess(filepath);
            return MyResponse.buildSuccess(recommendVOS);
        } catch (IOException e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }

    @Override
    public MyResponse getAllRecommends(String filepath) {
        try {
            ArrayList<ArrayList<AbbreviationRecommendVO>> paramAndVariableRecommends = HandleCSV.recommendProcess(filepath);

            ArrayList<MethodNameRecommendVO> methodRecommends = getMethodNameRecommends(filepath);
            ArrayList<AbbreviationRecommendVO> paramRecommends = paramAndVariableRecommends.get(0);
            ArrayList<AbbreviationRecommendVO> variableRecommends = paramAndVariableRecommends.get(1);
            // 提取所有的方法名作为推荐集合
            HashSet<String> allMethodNames = new HashSet<String>();
            for (MethodNameRecommendVO vo : methodRecommends) {
                allMethodNames.add(vo.getMethod_name());
            }
            for (AbbreviationRecommendVO vo : paramRecommends) {
                allMethodNames.add(vo.getMethod_name());
            }
            for (AbbreviationRecommendVO vo : variableRecommends) {
                allMethodNames.add(vo.getMethod_name());
            }
            // 将推荐集合转换为推荐映射
            HashMap<String, MethodBlockRecommendsVO> recommendMapByMethodName = new HashMap<String, MethodBlockRecommendsVO>();
            for (String methodName : allMethodNames) {
                recommendMapByMethodName.put(methodName, new MethodBlockRecommendsVO(methodName));
            }
            // 将所有推荐内容存入推荐映射中
            for (MethodNameRecommendVO vo : methodRecommends) {
                recommendMapByMethodName.get(vo.getMethod_name()).addToMethod_recommend_infos(vo);
            }
            for (AbbreviationRecommendVO vo : paramRecommends) {
                recommendMapByMethodName.get(vo.getMethod_name()).addToParam_recommend_infos(vo);
            }
            for (AbbreviationRecommendVO vo : variableRecommends) {
                recommendMapByMethodName.get(vo.getMethod_name()).addToVariable_recommend_infos(vo);
            }
            // 把推荐集合返回
            ArrayList<MethodBlockRecommendsVO> allRecommends = (ArrayList<MethodBlockRecommendsVO>) recommendMapByMethodName.values();
            return MyResponse.buildSuccess(allMethodNames);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyResponse.buildFailure("推荐获取失败");
    }

}
