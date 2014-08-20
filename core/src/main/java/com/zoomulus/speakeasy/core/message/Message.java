package com.zoomulus.speakeasy.core.message;

import java.nio.ByteBuffer;

public interface Message
{
    ByteBuffer buffer();
    String contentType();
}
