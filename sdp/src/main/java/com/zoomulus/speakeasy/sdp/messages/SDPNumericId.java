package com.zoomulus.speakeasy.sdp.messages;

import java.time.Instant;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import com.zoomulus.speakeasy.core.types.NumericString;

@EqualsAndHashCode(callSuper=true)
public class SDPNumericId extends NumericString
{
    public SDPNumericId()
    {
        // I realize I am getting the instant twice so they aren't exactly the
        // same instant, but I assert that for now it really doesn't matter
        // and it makes the hierarchy more beautiful. -MR
        super(Long.toString(Instant.now().getEpochSecond() << 32 + Instant.now().getNano()));
    }
    
    public SDPNumericId(@NonNull final String id)
    {
        super(id);
    }
}
