package com.zoomulus.speakeasy.sdp.messages.exceptions;

public class SDPParseException extends SDPMessageException
{
    private static final long serialVersionUID = -3687248154822526282L;
    
    public SDPParseException(final String msg)
    {
        super(msg);
    }
}