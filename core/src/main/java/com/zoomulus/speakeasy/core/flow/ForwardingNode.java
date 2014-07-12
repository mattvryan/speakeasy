package com.zoomulus.speakeasy.core.flow;

import java.util.Optional;

public interface ForwardingNode extends ReceivingNode
{
    String name();
    boolean hasSendTarget();
    Optional<String> sendTarget();
}
