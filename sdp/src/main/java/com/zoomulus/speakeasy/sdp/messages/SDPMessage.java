package com.zoomulus.speakeasy.sdp.messages;

import java.nio.ByteBuffer;

import lombok.Value;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

@Value
@Accessors(fluent=true)
public class SDPMessage implements Message
{
    final String contentType="application/sdp";
    final String version="0";
    final SDPOrigin origin;
    
    @Override
    public ByteBuffer buffer()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private SDPMessage()
    {
        origin = new SDPOrigin();
    }    

    public static SDPMessageBuilder builder()
    {
        return new SDPMessageBuilder();
    }
    
    public static class SDPMessageBuilder
    {
        public SDPMessage build()
        {
            return new SDPMessage();
        }
    }
}
