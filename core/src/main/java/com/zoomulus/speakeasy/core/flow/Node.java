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
public final class Node implements ReceivingNode, RespondingNode
{
    @Getter
    private final String name;    
    private final Processor processor;
    private final Optional<String> precedent;
    private final Optional<String> succedent;
    
    Node(@NonNull final String name,
            @NonNull final Processor processor,
            @NonNull final Optional<String> precedent,
            @NonNull final Optional<String> succedent)
    {
        this.name = name;
        this.processor = processor;
        this.precedent = precedent;
        this.succedent = succedent;
        this.processor.node(this);
    }
    
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private Flow flow;
    
    public boolean hasPrecedent()
    {
        return precedent.isPresent();
    }
    
    public boolean hasSuccedent()
    {
        return succedent.isPresent();
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
        private Optional<String> precedent = Optional.<String> empty();
        private Optional<String> succedent = Optional.<String> empty();
        
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
        
        public NodeBuilder precedent(final String precedentNodeName)
        {
            precedent = Optional.ofNullable(precedentNodeName);
            return this;
        }
        
        public NodeBuilder succedent(final String succedentNodeName)
        {
            succedent = Optional.ofNullable(succedentNodeName);
            return this;
        }
        
        public Node build()
        {
            return new Node(name, processor, precedent, succedent);
        }
    }
}
