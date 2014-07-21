package com.zoomulus.speakeasy.core.flow;

import lombok.Value;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;

@Value
@Accessors(fluent=true)
public class TestMessage implements Message
{
    final String buffer;
    final int type;    
}
