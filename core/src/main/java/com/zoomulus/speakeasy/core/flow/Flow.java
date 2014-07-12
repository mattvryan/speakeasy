package com.zoomulus.speakeasy.core.flow;

import java.util.List;
import java.util.Map;

import lombok.NonNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.zoomulus.speakeasy.core.message.Message;

/**
 * A Flow is used for handling messages and responses.
 * 
 * A Flow is created using a FlowBuilder, e.g.
 *   Flow.builder().build();
 * The builder requires:
 *   - Specification of a Source, which represents
 *     how data enters the flow
 *   - Specification of a Sink, which represents
 *     how response data exits the flow
 *   - Specification of one or more Nodes, which represent ordered
 *     processing steps for processing Messages
 *   - Identification of the first Node (where the Source
 *     sends received data)
 *   - Identification of the last Node (which sends its response
 *     data to the Sink)
 *
 */
public class Flow
{
    final Source source;
    final Sink sink;
    final Map<String, Node> nodes = Maps.newHashMap();
    final Multimap<String, String> sendTargets = HashMultimap.create();
    final Multimap<String, String> replyTargets = HashMultimap.create();
    
    Flow(@NonNull final Source source,
            @NonNull final Sink sink,
            @NonNull final List<Node> nodes)
    {
        source.flow(this);
        if (source.hasSendTarget())
        {
            sendTargets.put(source.name(), source.sendTarget().get());
        }
        
        sink.flow(this);
        
        for (Node node : nodes)
        {
            node.flow(this);
            this.nodes.put(node.name(), node);
            
            if (node.hasSendTarget())
            {
                sendTargets.put(node.name(), node.sendTarget().get());
            }
            
            if (node.hasReplyTarget())
            {
                replyTargets.put(node.name(), node.replyTarget().get());
            }
        }
        this.source = source;
        this.sink = sink;
    }
    
    public void relay(@NonNull final Message message,
            @NonNull final String sourceName)
    {
        for (final String receiverName : sendTargets.get(sourceName))
        {
            final Node receiver = nodes.get(receiverName);
            if (null != receiver)
            {
                receiver.processMessage(message);
                receiver.relay(message);
            }
        }
    }
    
    public void respond(@NonNull final Message response,
            @NonNull final String sourceName)
    {
        for (final String responderName : replyTargets.get(sourceName))
        {
            final Node responder = nodes.get(responderName);
            if (null != responder)
            {
                responder.processResponse(response);
                responder.respond(response);
            }
            else if (responderName.equals("sink"))
            {
                sink.processResponse(response);
            }
        }
    }
    
    public static FlowBuilder builder()
    {
        return new FlowBuilder();
    }
    
    public static class FlowBuilder
    {
        Source source;
        Sink sink;
        List<Node> nodes = Lists.newArrayList();

        public FlowBuilder source(@NonNull final Source source)
        {
            this.source = source;
            return this;
        }
        
        public FlowBuilder sink(@NonNull final Sink sink)
        {
            this.sink = sink;
            return this;
        }
        
        public FlowBuilder node(@NonNull final Node node)
        {
            return node(node, 0);
        }
        
        public FlowBuilder node(@NonNull final Node node, int position)
        {
            nodes.add(node);
            return this;
        }
        
        public Flow build()
        {
            return new Flow(source, sink, nodes);
        }
    }
}
