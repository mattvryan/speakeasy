package com.zoomulus.speakeasy.sdp.types;

import com.zoomulus.speakeasy.core.types.Word;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class SDPUsername extends Word
{
    public SDPUsername(final String username)
    {
        super(username);
    }
    
    public SDPUsername()
    {
        super("-");
    }
}
