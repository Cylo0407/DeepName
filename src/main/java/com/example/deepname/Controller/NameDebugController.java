package com.example.deepname.Controller;

import com.example.deepname.Service.NameDebugService;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.UserVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/nameDebug")
public class NameDebugController {

    @Resource
    private NameDebugService nameDebugService;

    /**
     * 接收文件
     */
    @PostMapping("/uploadZip")
    public MyResponse upload(@Valid @RequestBody MultipartFile file){
        return nameDebugService.upload(file);
    }

//    /**
//     * 接收文件夹
//     */
//    @PostMapping("/uploadFloder")
//    public MyResponse uploadFloder(@Valid @RequestBody MultipartFile[] files){
//        return nameDebugService.uploadFloder(files);
//    }

    /**
     * 通过url下载文件
     */
    @PostMapping("/git")
    public MyResponse downLoadFromUrl(@Valid @RequestBody String url){
        return nameDebugService.downLoadFromUrl(url);
    }

}
