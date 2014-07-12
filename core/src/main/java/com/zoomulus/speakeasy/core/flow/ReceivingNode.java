package com.zoomulus.speakeasy.core.flow;

import com.zoomulus.speakeasy.core.message.Message;

public interface ReceivingNode
{
    void processMessage(final Message message);
}
