package com.example.deepname.Utils.VPMapper;

import com.example.deepname.Entity.Record;
import com.example.deepname.VO.RecordVO;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RecordMapper {
    RecordMapper INSTANCE = Mappers.getMapper(RecordMapper.class);

    @Mappings({})
    RecordVO p2v(Record record);
    List<RecordVO> pList2vList(List<Record> recordList);

    @Mappings({})
    Record v2p(RecordVO recordVO);
    List<Record> vList2pList(List<RecordVO> recordVOList);
}
