package com.example.deepname.Service;

import com.example.deepname.Entity.Record;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.VO.RecordVO;

public interface RecordService {
    /**
     * 增加记录
     * @param recordVO 历史记录对象
     */
    RecordVO addRecord(RecordVO recordVO);

    /**
     * 修改记录
     * @param recordVO 历史记录对象
     */
    RecordVO updateRecord(RecordVO recordVO);

    /**
     * 按用户查找记录
     * @param username 用户名
     */
    MyResponse searchRecordsByUsername(String username);

    /**
     * 按id查找记录
     * @param id 查找id
     */
    MyResponse searchRecordsById(Integer id);

    /**
     * 删除记录
     * @param id 删除id
     */
    MyResponse deleteRecords(Integer id);
}
