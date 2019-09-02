package com.x.sdk.component.sequence.client.impl;

import com.x.sdk.component.sequence.client.ISeqClient;
import com.x.sdk.component.sequence.service.ISequenceService;
import com.x.sdk.component.sequence.service.impl.SequenceServiceImpl;

public class NormalSeqClientImpl implements ISeqClient {

    private ISequenceService sequenceService;

    public NormalSeqClientImpl() {
        this.sequenceService = new SequenceServiceImpl();
    }

    @Override
    public Long nextValue(String sequenceName) {
        return sequenceService.nextValue(sequenceName);
    }

}
