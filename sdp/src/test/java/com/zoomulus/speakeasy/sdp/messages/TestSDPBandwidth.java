package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zoomulus.speakeasy.sdp.messages.SDPBandwidth.SDPBandwidthType;

public class TestSDPBandwidth
{

    @Test
    public void testToString()
    {
        final SDPBandwidth bw = new SDPBandwidth(SDPBandwidthType.CT, 128);
        System.out.println(bw.toString());
        assertTrue("CT:128".equals(bw.toString()));
    }

}
