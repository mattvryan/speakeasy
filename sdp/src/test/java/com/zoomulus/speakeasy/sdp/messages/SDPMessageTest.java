package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import com.zoomulus.speakeasy.sdp.messages.exceptions.SDPMessageException;
import com.zoomulus.speakeasy.sdp.types.SDPOrigin;

public class SDPMessageTest
{
    SDPMessage defaultMessage;
    
    private void fail() { assertFalse(true); }
    
    @Before
    public void setup() throws UnknownHostException, SDPMessageException
    {
        defaultMessage = SDPMessage.builder()
                .origin(new SDPOrigin(Inet4Address.getLoopbackAddress()))
                .build();
    }
    
    @After
    public void teardown()
    {
        defaultMessage = null;
    }
    
    private ByteBuffer buildSimpleSdp(final Map<String, String> sdpValues)
    {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s=%s\n", "v", sdpValues.getOrDefault("v", SDPMessage.VERSION)));
        return ByteBuffer.wrap(builder.toString().getBytes());
    }
    
    @Test
    public void testBuildSDPMessage()
    {
        assertNotNull(defaultMessage);
    }
    
    @Test
    public void testContentTypeIsApplicationSDP()
    {
        assertEquals(MediaType.create("application", "sdp"), defaultMessage.contentType());
        assertEquals("application/sdp", defaultMessage.contentType().toString());
    }
    
    @Test
    public void testVersionIs0()
    {
        assertEquals("0", defaultMessage.version());
    }
    
    @Test
    public void testParseMissingVersionFails() throws IOException
    {
        try
        {
            final Map<String, String> sdpValues = Maps.newHashMap();
            sdpValues.put(SDPMessage.Tokens.VERSION_TOKEN, "");
            SDPMessage.from(buildSimpleSdp(sdpValues));
            fail();
        }
        catch (SDPMessageException e) { }
    }
    
    @Test
    public void testParseInvalidVersionFails()
    {
        
    }
    
    @Test
    public void testParseValidVersion()
    {
        
    }
}
