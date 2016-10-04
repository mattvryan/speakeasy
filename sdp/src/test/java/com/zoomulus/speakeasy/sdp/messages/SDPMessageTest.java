package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import com.zoomulus.speakeasy.core.types.AddrType;
import com.zoomulus.speakeasy.core.types.LocalInetAddress;
import com.zoomulus.speakeasy.sdp.messages.exceptions.SDPMessageException;
import com.zoomulus.speakeasy.sdp.types.SDPOrigin;

public class SDPMessageTest
{
    SDPMessage defaultMessage;
    SDPOrigin defaultOrigin;
    
    private void fail() { assertFalse(true); }
    
    @Before
    public void setup() throws UnknownHostException, SDPMessageException
    {
        defaultMessage = SDPMessage.builder()
                .origin(new SDPOrigin(Inet4Address.getLoopbackAddress()))
                .build();
        Optional<InetAddress> inetAddress = LocalInetAddress.guess(AddrType.IP4);
        defaultOrigin = new SDPOrigin(inetAddress.isPresent() ? inetAddress.get() : InetAddress.getByName("127.0.0.1"));
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
        builder.append(String.format("%s=%s\n", "o", sdpValues.getOrDefault("o", defaultOrigin.toString())));
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
    public void testParseInvalidVersionFails()
    {
        for (final String s : Lists.newArrayList("", null, "1", "invalid"))
        {
            try
            {
                final Map<String, String> sdpValues = Maps.newHashMap();
                sdpValues.put(SDPMessage.Tokens.VERSION_TOKEN, s);
                SDPMessage.parse(buildSimpleSdp(sdpValues));
                fail();
            }
            catch (SDPMessageException e) { }
        }
    }
    
    @Test
    public void testParseValidVersion() throws SDPMessageException
    {
        final Map<String, String> sdpValues = Maps.newHashMap();
        sdpValues.put(SDPMessage.Tokens.VERSION_TOKEN, "0");
        SDPMessage.parse(buildSimpleSdp(sdpValues));
    }
    
    @Test
    public void testParseInvalidOriginFails()
    {
        for (final String s : Lists.newArrayList("", null, "invalid", "- 12345 54321 OUT IP4 127.0.0.1", "- 12345 54321 IN IP7 127.0.0.1"))
        {
            try
            {
                final Map<String, String> sdpValues = Maps.newHashMap();
                sdpValues.put(SDPMessage.Tokens.ORIGIN_TOKEN, s);
                SDPMessage.parse(buildSimpleSdp(sdpValues));
                fail();
            }
            catch (SDPMessageException e) { }
        }
    }
    
    @Test
    public void testParseValidOrigin() throws SDPMessageException
    {
        final Map<String, String> sdpValues = Maps.newHashMap();
        sdpValues.put(SDPMessage.Tokens.ORIGIN_TOKEN, "- 12345 54321 IN IP4 127.0.0.1");
        SDPMessage.parse(buildSimpleSdp(sdpValues));
    }
}
