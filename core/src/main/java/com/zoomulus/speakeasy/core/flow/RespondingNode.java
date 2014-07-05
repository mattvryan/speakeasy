package com.zoomulus.speakeasy.core.flow;

import com.zoomulus.speakeasy.core.message.Message;

public interface RespondingNode
{
    String name();
    void processResponse(final Message response);
}
