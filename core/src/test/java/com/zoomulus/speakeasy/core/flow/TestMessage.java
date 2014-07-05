package com.zoomulus.speakeasy.core.flow;

import lombok.Value;

import com.zoomulus.speakeasy.core.message.Message;

@Value
public class TestMessage implements Message
{
    final String buffer;
    final int type;
}
