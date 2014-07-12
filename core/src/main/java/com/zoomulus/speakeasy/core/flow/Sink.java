package com.zoomulus.speakeasy.core.flow;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

/**
 * A Sink sends a response to some other source besides
 * a Node.  A Flow ends with a Sink.
 */
@Data
@Accessors(fluent=true)
public class Sink
{
    private final String name = "sink"; 
    private final Replier replier;
    @Setter(AccessLevel.PACKAGE)
    private Flow flow;

    private Sink(final Replier replier)
    {
        this.replier = replier;
    }
    
    public void processResponse(final Message response)
    {
        replier.onReplyReceived(response);
    }
    
    public static SinkBuilder builder()
    {
        return new SinkBuilder();
    }
    
    public static class SinkBuilder
    {
        private Replier replier;
        
        public SinkBuilder responder(final Replier replier)
        {
            this.replier = replier;
            return this;
        }
        
        public Sink build()
        {
            return new Sink(replier);
        }
    }
}
