package com.example.deepname.Repository;

import com.example.deepname.Entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    /**
     * 根据用户名查询记录
     */
    List<Record> getRecordsByUsername(String username);

    /**
     * 根据id查询记录
     */
    Record getRecordsById(Integer id);

}
