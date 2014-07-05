package com.zoomulus.speakeasy.core.flow;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;


/**
 * A Source receives input from somewhere other than 
 * another Node.  A Flow begins with a Source.
 */
@Data
@Accessors(fluent=true)
public class Source implements ReceivingNode
{
    private final String name = "source"; 
    @Setter(AccessLevel.PACKAGE)
    private Flow flow;

    @Override
    public final void processMessage(final Message message)
    {
        relay(message);
    }

    private void relay(final Message message)
    {
        flow.relay(message, "source");
    }
}
