package com.zoomulus.speakeasy.core.flow;

import com.zoomulus.speakeasy.core.message.Message;

public interface Replier
{
    void onReplyReceived(final Message response);
}
