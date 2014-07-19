package com.zoomulus.speakeasy.core.flow;

import java.util.List;

public interface ForwardingNode extends ReceivingNode
{
    String name();
//    boolean hasSendTarget();
    List<String> sendTargets();
}
