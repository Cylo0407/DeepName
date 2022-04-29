package com.example.deepname.Controller;

import com.example.deepname.Service.FileService;
import com.example.deepname.Utils.MyResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 接收文件
     */
    @PostMapping("/uploadZip")
    public MyResponse upload(@RequestParam("file") MultipartFile file,
                             @RequestParam("username") String username) {
        return fileService.upload(username, file);
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
    @RequestMapping("/git")
    public MyResponse downLoadFromUrl(@RequestParam(value = "username") String username,
                                      @RequestParam(value = "url") String url) {
        return fileService.downLoadFromUrl(username, url);
    }

    /**
     * 遍历文件目录,获得下级目录结构
     */
    @GetMapping("/dir")
    public MyResponse getDir(@RequestParam(value = "dirpath") String dirpath) {
        return fileService.getDir(dirpath);
    }

    /**
     * 返回文件内容
     */
    @GetMapping("/ctx")
    public MyResponse getFileCtx(@RequestParam(value = "filepath") String filepath) {
        return fileService.getFileCtx(filepath);
    }

    /**
     * 调用python服务
     */
    @GetMapping("/gtnm")
    public MyResponse getPyService(@RequestParam(value = "id") Integer id) {
        return fileService.getPyService(id);
    }

}
