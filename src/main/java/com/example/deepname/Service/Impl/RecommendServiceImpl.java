package com.example.deepname.Service.Impl;

import abbrivatiate_expander.src.Step2.HandleCSV;
import com.example.deepname.Service.RecommendService;
import com.example.deepname.Utils.Levenshtein;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.Utils.PythonRunner;
import com.example.deepname.VO.AbbreviationRecommendVO;
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
            while ((line = outputReader.readLine()) != null) {
                String[] names = line.split(",");
                float distance = Levenshtein.getSimilarity(names[0], names[1]);
                resList.add(new MethodNameRecommendVO(names[0], names[1], distance));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyResponse.buildSuccess(resList);
    }

    @Override
    public MyResponse getParamExpand(String filepath) {
        ArrayList<AbbreviationRecommendVO> recommendVOS;
        try {
            recommendVOS = HandleCSV.recommendProcess(filepath);
            return MyResponse.buildSuccess(recommendVOS);
        } catch (IOException e) {
            e.printStackTrace();
            return MyResponse.buildFailure(e.getMessage());
        }
    }

}
