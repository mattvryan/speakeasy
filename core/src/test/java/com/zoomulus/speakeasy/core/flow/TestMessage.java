package com.zoomulus.speakeasy.core.flow;

import java.nio.ByteBuffer;

import lombok.Value;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

@Value
@Accessors(fluent=true)
public class TestMessage implements Message
{
    final ByteBuffer buffer;
    final int type;
    
    public TestMessage(final String contents, final int type)
    {
        buffer = ByteBuffer.allocate(contents.length());
        buffer.put(contents.getBytes());
        buffer.rewind();
        this.type = type;
    }
    
    public ByteBuffer buffer()
    {
        return buffer.duplicate();
    }
    
    public String contents()
    {
        return new String(buffer.array());
    }
}
