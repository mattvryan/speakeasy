package com.zoomulus.speakeasy.core.message;

import java.nio.ByteBuffer;

import com.google.common.net.MediaType;

public interface Message
{
    ByteBuffer buffer();
    MediaType contentType();
}
