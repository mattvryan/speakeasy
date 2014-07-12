package com.zoomulus.speakeasy.core.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestBasicFlow
{
    @Test
    public void testSendValidBufferGeneratesExpectedResponse()
    {
        Receiver receiver = new TestReceiver();
        TestResponder responder = new TestResponder();
        TestMessageTypeExaminer typeExaminer = new TestMessageTypeExaminer();
        TestMessageBufferExaminer bufferExaminer = new TestMessageBufferExaminer();
        
        Source source = Source.builder()
                .receiver(receiver)
                .sendsTo("typeExaminer")
                .build();
        Sink sink = Sink.builder()
                .responder(responder)
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
        
        assertNull(responder.response());
        
        receiver.onMessageReceived(message);
        
        assertEquals(message, responder.response());
        assertEquals(5, typeExaminer.type());
        assertEquals("Test Message", bufferExaminer.buffer());
    }
}
