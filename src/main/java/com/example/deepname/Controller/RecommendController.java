package com.example.deepname.Controller;

import com.example.deepname.Service.RecommendService;
import com.example.deepname.Utils.MyResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Resource
    private RecommendService recommendService;

    /**
     * 调用python服务
     */
    @GetMapping("/gtnm")
    public MyResponse getPythonMethod(@RequestParam(value = "filepath") String filepath) {
        return recommendService.getPyService(filepath);
    }

    /**
     * 调用参数拓展
     *
     * @param filepath filepath
     * @return 返回一个ParamRecommend的数组
     */
    @GetMapping("/paramRecommend")
    public MyResponse getParamExpand(@RequestParam(value = "filepath") String filepath) {
        return recommendService.getParamExpand(filepath);
    }


    @GetMapping("/recommendAll")
    public MyResponse getAllRecommends(@RequestParam(value = "filepath") String filepath) {
        return recommendService.getAllRecommends(filepath);
    }

}
