package com.zoomulus.speakeasy.core.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.zoomulus.speakeasy.core.message.Message;

public class TestBasicFlow
{
    @Test
    public void testSendValidBufferGeneratesExpectedResponse()
    {
        Receiver receiver = new TestReceiver();
        TestSender sender = new TestSender();
        TestMessageTypeExaminer typeExaminer = new TestMessageTypeExaminer();
        TestMessageBufferExaminer bufferExaminer = new TestMessageBufferExaminer();
        
        Source source = Source.builder()
                .receiver(receiver)
                .sendsTo("typeExaminer")
                .build();
        Sink sink = Sink.builder()
                .sender(sender)
                .build();
        Node typeExaminerNode = Node.builder()
                .name("typeExaminer")
                .processor(typeExaminer)
                .sendsTo("bufferExaminer")
                .repliesTo(sink.name())
                .build();
        Node bufferExaminerNode = Node.builder()
                .name("bufferExaminer")
                .processor(bufferExaminer)
                .repliesTo("typeExaminer")
                .build();
        
        @SuppressWarnings("unused")
        Flow flow = Flow.builder()
            .source(source)
            .sink(sink)
            .node(typeExaminerNode)
            .node(bufferExaminerNode)
            .build();
        
        TestMessage message = new TestMessage("Test Message", 5);
        
        assertNull(sender.response());
        
        receiver.onMessageReceived(message);
        
        assertEquals(message, sender.response());
        assertEquals(5, typeExaminer.type());
        assertEquals("Test Message", bufferExaminer.buffer());
    }
    
    @Test
    public void testProxyMode()
    {
        Receiver receiver = new TestReceiver();
        TestSender sender = new TestSender();
        TestMessageTypeExaminer typeExaminer = new TestMessageTypeExaminer();

        Source source = Source.builder()
                .receiver(receiver)
                .sendsTo("typeExaminer")
                .build();
        Sink sink = Sink.builder()
                .sender(sender)
                .build();
        Node typeExaminerNode = Node.builder()
                .name("typeExaminer")
                .processor(typeExaminer)
                .sendsTo(sink.name())
                .build();
        
        @SuppressWarnings("unused")
        Flow flow = Flow.builder()
            .source(source)
            .sink(sink)
            .node(typeExaminerNode)
            .build();

        TestMessage message = new TestMessage("Test Message", 5);
        
        assertNull(sender.response());
        
        receiver.onMessageReceived(message);
        
        assertEquals(message, sender.message());
        assertEquals(5, typeExaminer.type());
    }
    
    @Test
    public void testFlowWithBranch()
    {
        Receiver receiver = new TestReceiver();
        TestSender sender = new TestSender();
        TestMessageTypeExaminer leftNodeProcessor = new TestMessageTypeExaminer();
        TestMessageTypeExaminer rightNodeProcessor = new TestMessageTypeExaminer();
        
        Source source = Source.builder()
                .receiver(receiver)
                .sendsTo("branch")
                .build();
        Sink sink = Sink.builder()
                .sender(sender)
                .build();
        Node branchingNode = Node.builder()
                .name("branch")
                .sendsTo(Lists.newArrayList("leftNode", "rightNode"))
                .repliesTo(sink.name())
                .build();
        Node leftNode = Node.builder()
                .name("leftNode")
                .processor(leftNodeProcessor)
                .repliesTo("branch")
                .build();
        Node rightNode = Node.builder()
                .name("rightNode")
                .processor(rightNodeProcessor)
                .repliesTo("branch")
                .build();
        
        @SuppressWarnings("unused")
        Flow flow = Flow.builder()
            .source(source)
            .sink(sink)
            .node(branchingNode)
            .node(leftNode)
            .node(rightNode)
            .build();        

        TestMessage message = new TestMessage("Test Message", 5);
        
        assertNull(sender.response());
        
        receiver.onMessageReceived(message);
        
        assertEquals(message, sender.response());
        assertEquals(5, leftNodeProcessor.type());
        assertEquals(5, rightNodeProcessor.type());
    }
    
    @Test
    public void testFlowWithConditionalBranch()
    {
        
    }
}
