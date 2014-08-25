package com.zoomulus.speakeasy.sdp.messages;

import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
public class SDPBandwidth
{
    public enum SDPBandwidthType
    {
        CT, AS;
    }

    @Getter
    @Accessors(fluent = true)
    private final SDPBandwidthType bwtype;
    @Getter
    @Accessors(fluent = true)
    private final int bandwidth;

    @Override
    public String toString()
    {
        return String.format("%s:%d", bwtype, bandwidth);
    }
}
