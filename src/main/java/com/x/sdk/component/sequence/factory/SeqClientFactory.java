package com.x.sdk.component.sequence.factory;

import com.x.sdk.component.sequence.client.ISeqClient;
import com.x.sdk.component.sequence.client.impl.NormalSeqClientImpl;

public final class SeqClientFactory {

    private SeqClientFactory() {

    }

    private static ISeqClient sequenceClient;

    public static ISeqClient getSeqClient() {
        if (sequenceClient == null) {
        	synchronized(SeqClientFactory.class){
        		if(sequenceClient == null){
        			sequenceClient = new NormalSeqClientImpl();
        		}        		
        	}
        }
        return sequenceClient;
    }
}
