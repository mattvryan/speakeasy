package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSDPMessage
{
    @Test
    public void testBuildSDPMessage()
    {
        SDPMessage message = SDPMessage.builder().build();
        assertNotNull(message);
    }
}
