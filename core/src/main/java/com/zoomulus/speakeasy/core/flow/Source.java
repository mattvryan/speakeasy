package com.zoomulus.speakeasy.core.flow;

import java.util.Optional;

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
    private final Receiver receiver;
    private final Optional<String> sendTarget;
    @Setter(AccessLevel.PACKAGE)
    private Flow flow;
    
    private Source(final Receiver receiver, final String downstreamNodeName)
    {
        this.receiver = receiver;
        receiver.source(this);
        this.sendTarget = Optional.of(downstreamNodeName);
    }
    
    @Override
    public boolean hasSendTarget()
    {
        return sendTarget.isPresent();
    }
    
    @Override
    public final void processMessage(final Message message)
    {
        relay(message);
    }

    private void relay(final Message message)
    {
        flow.relay(message, name);
    }
    
    public static SourceBuilder builder()
    {
        return new SourceBuilder();
    }
    
    public static class SourceBuilder
    {
        private Receiver receiver;
        private String sendTarget;
        
        public SourceBuilder receiver(final Receiver receiver)
        {
            this.receiver = receiver;
            return this;
        }
        
        public SourceBuilder sendsTo(final String sendTarget)
        {
            this.sendTarget = sendTarget;
            return this;
        }
        
        public Source build()
        {
            return new Source(receiver, sendTarget);
        }
    }
}
