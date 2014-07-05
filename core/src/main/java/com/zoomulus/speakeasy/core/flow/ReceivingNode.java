package com.zoomulus.speakeasy.core.flow;

import com.zoomulus.speakeasy.core.message.Message;

public interface ReceivingNode
{
    String name();
    void processMessage(final Message message);
}
