package com.x.sdk.component.sequence.dao;

import java.util.List;

import com.x.sdk.component.sequence.model.Sequence;
import com.x.sdk.component.sequence.model.SequenceCache;

public interface ISequenceDao {

    SequenceCache getSequenceCache(String sequenceName);

    List<Sequence> queryAllSequence();

    Sequence querySequenceByName(String sequenceName);

    void modifySequence(String sequenceName, long nextVal);

}
