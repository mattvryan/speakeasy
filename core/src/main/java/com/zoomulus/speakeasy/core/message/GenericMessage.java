package com.zoomulus.speakeasy.core.message;

import java.nio.ByteBuffer;

public class GenericMessage implements Message
{
    private ByteBuffer buffer;
    
    private GenericMessage(final ByteBuffer buffer)
    {
        this.buffer = buffer;
    }
    
    public static Message withString(final String in)
    {
        ByteBuffer buf = ByteBuffer.allocate(in.length());
        buf.put(in.getBytes());
        return new GenericMessage(buf);
    }
    
    public static Message withBuffer(final ByteBuffer in)
    {
        return new GenericMessage(in);
    }
}
