package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSDPMessage
{
    SDPMessage defaultMessage;
    
    @Before
    public void setup()
    {
        defaultMessage = SDPMessage.builder().build();
    }
    
    @After
    public void teardown()
    {
        defaultMessage = null;
    }
    
    @Test
    public void testBuildSDPMessage()
    {
        assertNotNull(defaultMessage);
    }
    
    @Test
    public void testContentTypeIsApplicationSDP()
    {
        assertEquals("application/sdp", defaultMessage.contentType());
    }
    
    @Test
    public void testVersionIs0()
    {
        assertEquals("0", defaultMessage.version());
    }
}
