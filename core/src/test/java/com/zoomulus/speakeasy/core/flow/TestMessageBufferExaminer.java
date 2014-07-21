package com.zoomulus.speakeasy.core.flow;

import lombok.Data;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

@Data
@Accessors(fluent=true)
public class TestMessageBufferExaminer implements Processor
{
    @Accessors(fluent=true, chain=false)
    private Node node;
    
    private String buffer;
    
    @Override
    public void handleMessage(Message message)
    {
        if (message instanceof TestMessage)
        {
            buffer = ((TestMessage) message).buffer();
        }
        node().respond(message);
    }

    @Override
    public void handleResponse(Message response)
    {
    }
}
