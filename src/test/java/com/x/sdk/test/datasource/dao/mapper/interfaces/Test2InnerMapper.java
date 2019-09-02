package com.x.sdk.test.datasource.dao.mapper.interfaces;

import com.x.sdk.test.datasource.dao.bo.Test2Inner;
import com.x.sdk.test.datasource.dao.bo.Test2InnerCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface Test2InnerMapper {
    int countByExample(Test2InnerCriteria example);

    int deleteByExample(Test2InnerCriteria example);

    int insert(Test2Inner record);

    int insertSelective(Test2Inner record);

    List<Test2Inner> selectByExample(Test2InnerCriteria example);

    int updateByExampleSelective(@Param("record") Test2Inner record, @Param("example") Test2InnerCriteria example);

    int updateByExample(@Param("record") Test2Inner record, @Param("example") Test2InnerCriteria example);
}