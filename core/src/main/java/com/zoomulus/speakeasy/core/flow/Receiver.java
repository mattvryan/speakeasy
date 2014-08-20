package com.zoomulus.speakeasy.core.flow;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

public abstract class Receiver
{
    @Setter(AccessLevel.PACKAGE)
    @Accessors(fluent=true)
    Source source;
    
    protected void onMessageReceived(final Message message)
    {
        source.processMessage(message);
    }
}
