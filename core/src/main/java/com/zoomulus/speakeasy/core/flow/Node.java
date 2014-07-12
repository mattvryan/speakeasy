package com.zoomulus.speakeasy.core.flow;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

/**
 * A Node represents a step in a Flow where message
 * or response processing can occur.
 * A Node does not perform any message processing;
 * instead, that is done by a Processor that was supplied
 * to the Node at construction time.
 */
@Data
@Accessors(fluent=true)
public final class Node implements ForwardingNode, ReplyingNode
{
    @Getter
    private final String name;    
    private final Processor processor;
    private final Optional<String> replyTarget;
    private final Optional<String> sendTarget;
    
    Node(@NonNull final String name,
            @NonNull final Processor processor,
            @NonNull final Optional<String> replyTarget,
            @NonNull final Optional<String> sendTarget)
    {
        this.name = name;
        this.processor = processor;
        this.replyTarget = replyTarget;
        this.sendTarget = sendTarget;
        this.processor.node(this);
    }
    
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private Flow flow;
    
    @Override
    public boolean hasReplyTarget()
    {
        return replyTarget.isPresent();
    }
    
    @Override
    public boolean hasSendTarget()
    {
        return sendTarget.isPresent();
    }
    
    protected void relay(@NonNull final Message message)
    {
        flow().relay(message, name());
    }
    
    protected void respond(@NonNull final Message message)
    {
        flow().respond(message, name());
    }
    
    @Override
    public void processMessage(Message message)
    {
        processor.handleMessage(message);
    }
    
    @Override
    public void processResponse(Message response)
    {
        processor.handleResponse(response);
    }
    
    public static NodeBuilder builder()
    {
        return new NodeBuilder();
    }
    
    public static class NodeBuilder
    {
        private String name;
        private Processor processor;
        private Optional<String> replyTarget = Optional.<String> empty();
        private Optional<String> sendTarget = Optional.<String> empty();
        
        public NodeBuilder name(@NonNull final String name)
        {
            this.name = name;
            return this;
        }
        
        public NodeBuilder processor(@NonNull final Processor processor)
        {
            this.processor = processor;
            return this;
        }
        
        public NodeBuilder repliesTo(final String replyTarget)
        {
            this.replyTarget = Optional.ofNullable(replyTarget);
            return this;
        }
        
        public NodeBuilder sendsTo(final String sendTarget)
        {
            this.sendTarget = Optional.ofNullable(sendTarget);
            return this;
        }
        
        public Node build()
        {
            return new Node(name, processor, replyTarget, sendTarget);
        }
    }
}
