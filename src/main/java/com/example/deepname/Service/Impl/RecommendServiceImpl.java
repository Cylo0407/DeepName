package com.example.deepname.Service.Impl;

import abbrivatiate_expander.src.Step2.HandleCSV;
import com.example.deepname.Service.RecommendService;
import com.example.deepname.Utils.*;
import com.example.deepname.VO.AbbreviationRecommendVO;
import com.example.deepname.VO.MethodBlockRecommendsVO;
import com.example.deepname.VO.MethodNameRecommendVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;

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
                String location = utils.getLocation(filepath, names[0], signatures.get(idx));
                ArrayList<String> method_params = utils.getSignatureParams(names[0], signatures.get(idx));
                resList.add(new MethodNameRecommendVO(names[0], signatures.get(idx).replace("$", "\n"), names[1], utils.getAccuracyType(distance), location, method_params));
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

            ArrayList<MethodBlockRecommendsVO> allRecommends = new ArrayList<MethodBlockRecommendsVO>();
            // 提取所有的方法名作为推荐集合
            for (MethodNameRecommendVO vo : methodRecommends) {
                allRecommends.add(new MethodBlockRecommendsVO(vo.getMethod_name(), vo));
            }
            // 将所有推荐参数和变量加入方法
            for (MethodBlockRecommendsVO methodBlockRecommendsVO : allRecommends) {
                for (AbbreviationRecommendVO vo : paramRecommends) {
                    if (methodBlockRecommendsVO.getMethod_name().toLowerCase().equals(vo.getMethod_name().toLowerCase())
                            && methodBlockRecommendsVO.getMethod_recommend_infos().getMethod_location().equals(vo.getParam_location())) {
                        methodBlockRecommendsVO.addToParam_recommend_infos(vo);
                    }
                }
                for (AbbreviationRecommendVO vo : variableRecommends) {
                    if (methodBlockRecommendsVO.getMethod_name().toLowerCase().equals(vo.getMethod_name().toLowerCase())) {
                        methodBlockRecommendsVO.addToVariable_recommend_infos(vo);
                    }
                }
            }
            // 把推荐集合返回
            return MyResponse.buildSuccess(allRecommends);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyResponse.buildFailure("推荐获取失败");
    }


}
