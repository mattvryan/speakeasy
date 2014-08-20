package com.zoomulus.speakeasy.core.flow;

import java.util.Optional;

import com.zoomulus.speakeasy.core.message.Message;

public interface ReplyingNode
{
    String name();
    boolean hasReplyTarget();
    Optional<String> replyTarget();
    void processResponse(final Message response);
}
