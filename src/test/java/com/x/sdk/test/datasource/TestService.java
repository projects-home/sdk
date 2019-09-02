package com.x.sdk.test.datasource;

import com.x.sdk.test.datasource.dao.mapper.interfaces.Test2InnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x.sdk.test.datasource.dao.bo.Test2InnerCriteria;

@Service
public class TestService {
    @Autowired
    private Test2InnerMapper gpMapper;

    public int getGPData() {
        Test2InnerCriteria example = new Test2InnerCriteria();
        int result = gpMapper.countByExample(example);
        return result;
    }
}
