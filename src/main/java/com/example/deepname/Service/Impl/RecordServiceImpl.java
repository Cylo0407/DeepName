package com.example.deepname.Service.Impl;

import com.example.deepname.Entity.Record;
import com.example.deepname.Exception.BussinessException;
import com.example.deepname.Repository.RecordRepository;
import com.example.deepname.Service.RecordService;
import com.example.deepname.Utils.MyResponse;
import com.example.deepname.Utils.VPMapper.RecordMapper;
import com.example.deepname.VO.RecordVO;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordRepository recordRepository;

    public RecordVO addRecord(RecordVO recordVO) {
        try {
            RecordVO rv = RecordMapper.INSTANCE.p2v(recordRepository.save(RecordMapper.INSTANCE.v2p(recordVO)));
            return rv;
        } catch (DataAccessException e) {
            throw new BussinessException("创建记录失败！");
        }
    }

    @Override
    public RecordVO updateRecord(RecordVO recordVO) {
        try {
            RecordVO rv = RecordMapper.INSTANCE.p2v(recordRepository.save(RecordMapper.INSTANCE.v2p(recordVO)));
            return rv;
        } catch (DataAccessException e) {
            throw new BussinessException("修改记录失败！");
        }
    }

    @Override
    public MyResponse searchRecordsByUsername(String username) {
        List<Record> recordList = recordRepository.getRecordsByUsername(username);
        return MyResponse.buildSuccess(recordList);
    }

    @Override
    public MyResponse searchRecordsById(Integer id) {
        Record record = recordRepository.getRecordsById(id);
        return MyResponse.buildSuccess(record);
    }

    @Override
    public MyResponse deleteRecords(Integer id) {
        try {
            recordRepository.deleteById(id);
            return MyResponse.buildSuccess();
        } catch (DataAccessException e) {
            return MyResponse.buildFailure("删除记录失败！");
        }
    }


}
