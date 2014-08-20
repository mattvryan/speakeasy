package com.zoomulus.speakeasy.core.flow;

import com.zoomulus.speakeasy.core.message.Message;

public interface Sender
{
    void onMessageRecevied(final Message message);
    void onReplyReceived(final Message response);
}
