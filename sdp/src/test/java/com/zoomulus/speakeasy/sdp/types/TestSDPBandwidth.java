package com.zoomulus.speakeasy.sdp.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.zoomulus.speakeasy.sdp.types.SDPBandwidth;
import com.zoomulus.speakeasy.sdp.types.SDPBandwidth.SDPBandwidthType;

public class TestSDPBandwidth
{

    @Test
    public void testToString()
    {
        final SDPBandwidth bw = new SDPBandwidth(SDPBandwidthType.CT, 128);
        assertTrue("CT:128".equals(bw.toString()));
    }

    @Test
    public void testParseValid()
    {
        final Optional<SDPBandwidth> bw = SDPBandwidth.fromString("CT:128");
        assertTrue("CT:128".equals(bw.get().toString()));
    }

    @Test
    public void testParseInvalid()
    {
        final Optional<SDPBandwidth> bw = SDPBandwidth.fromString("BOB:128");
        assertFalse(bw.isPresent());
        final Optional<SDPBandwidth> bw2 = SDPBandwidth.fromString("CT:BILL");
        assertFalse(bw2.isPresent());
    }

}
