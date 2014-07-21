package com.zoomulus.speakeasy.sdp.messages;

import java.nio.ByteBuffer;

import com.zoomulus.speakeasy.core.message.Message;

public class SDPMessage implements Message
{
    @Override
    public ByteBuffer buffer()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private SDPMessage()
    {
        
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
