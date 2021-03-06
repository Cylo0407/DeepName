package com.example.deepname.Controller;

import com.example.deepname.Service.RecordService;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.RecordVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@CrossOrigin(value = "http://localhost:8080")
@RestController
@RequestMapping("/record")
public class RecordController {
    @Resource
    private RecordService recordService;

    /**
     * 增加记录
     */
    @PostMapping("/add")
    public MyResponse addRecord(@Valid @RequestBody RecordVO recordVO){
        return MyResponse.buildSuccess(recordService.addRecord(recordVO));
    }

    /**
     * 修改记录
     */
    @PostMapping("/update")
    public MyResponse updateRecord(@Valid @RequestBody RecordVO recordVO){
        return MyResponse.buildSuccess(recordService.updateRecord(recordVO));
    }


    /**
     * 按用户名查找记录
     */
    @GetMapping("/nameSearch")
    public MyResponse searchRecordsByUsername(@RequestParam(value = "username") String username) {
        return recordService.searchRecordsByUsername(username);
    }

    /**
     * 按id查找记录
     */
    @GetMapping("/idSearch")
    public MyResponse searchRecordsById(@RequestParam(value = "id") Integer id) {
        return recordService.searchRecordsById(id);
    }

    /**
     * 删除记录
     */
    @GetMapping("/delete")
    public MyResponse deleteRecord(@RequestParam(value = "id") Integer id) {
        return recordService.deleteRecords(id);
    }

}
