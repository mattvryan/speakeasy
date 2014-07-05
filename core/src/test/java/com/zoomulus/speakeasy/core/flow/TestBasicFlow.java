package com.zoomulus.speakeasy.core.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestBasicFlow
{

    @Test
    public void testSendValidBufferGeneratesExpectedResponse()
    {
        TestMessageTypeExaminer typeExaminer = new TestMessageTypeExaminer();
        TestMessageBufferExaminer bufferExaminer = new TestMessageBufferExaminer();
        
        TestListener source = new TestListener();
        TestResponder sink = new TestResponder();
        Node typeExaminerNode = Node.builder()
                .name("typeExaminer")
                .processor(typeExaminer)
                .succedent("bufferExaminer")
                .build();
        Node bufferExaminerNode = Node.builder()
                .name("bufferExaminer")
                .processor(bufferExaminer)
                .precedent("typeExaminer")
                .build();
        
        @SuppressWarnings("unused")
        Flow flow = Flow.builder()
            .source(source)
            .sink(sink)
            .node(typeExaminerNode,
                    (Flow.FlowBuilder.FIRST_RECEIVER | Flow.FlowBuilder.LAST_RESPONDER))
            .node(bufferExaminerNode)
            .build();
        
        TestMessage message = new TestMessage("Test Message", 5);
        
        assertNull(sink.response());
        
        source.processMessage(message);
        
        assertEquals(message, sink.response());
        assertEquals(5, typeExaminer.type());
        assertEquals("Test Message", bufferExaminer.buffer());
    }
}
