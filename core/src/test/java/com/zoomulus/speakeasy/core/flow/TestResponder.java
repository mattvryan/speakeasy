package com.zoomulus.speakeasy.core.flow;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

@Getter
@Accessors(fluent=true)
public class TestResponder implements Replier
{
    private Message response;

    @Override
    public void onReplyReceived(Message response)
    {
        this.response = response;
    }
}
