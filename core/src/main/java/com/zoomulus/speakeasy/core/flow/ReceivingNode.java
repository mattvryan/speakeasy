package com.zoomulus.speakeasy.core.flow;

import java.util.Optional;

import com.zoomulus.speakeasy.core.message.Message;

public interface ReceivingNode
{
    String name();
    boolean hasSendTarget();
    Optional<String> sendTarget();
    void processMessage(final Message message);
}
