package com.zoomulus.speakeasy.sdp.messages;

import java.util.Optional;

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

    public static Optional<SDPBandwidth> fromString(final String stringrep)
    {
        Optional<SDPBandwidth> result = Optional.<SDPBandwidth> empty();
        final String[] parts = stringrep.split(":");
        if (parts.length == 2)
        {
            try
            {
                final SDPBandwidthType bwtype = SDPBandwidthType
                        .valueOf(parts[0]);
                final int bandwidth = Integer.valueOf(parts[1]);
                result = Optional.of(new SDPBandwidth(bwtype, bandwidth));
            }
            catch (final IllegalArgumentException e)
            {
            }
        }
        return result;
    }
}
