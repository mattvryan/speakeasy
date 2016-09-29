package com.zoomulus.speakeasy.sdp.messages.exceptions;

public class SDPMessageException extends Exception
{
    private static final long serialVersionUID = 5792916194152257510L;

    public SDPMessageException(final String msg)
    {
        super(msg);
    }
    
    public SDPMessageException(final String msg, final Exception e)
    {
        super(msg, e);
    }
    
    public SDPMessageException(final Exception e)
    {
        super(e);
    }
}
