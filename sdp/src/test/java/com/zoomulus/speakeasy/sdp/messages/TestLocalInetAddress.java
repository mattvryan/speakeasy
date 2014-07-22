package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zoomulus.speakeasy.core.util.AddrType;
import com.zoomulus.speakeasy.core.util.LocalInetAddress;

public class TestLocalInetAddress
{

    @Test
    public void test()
    {
        assertTrue(LocalInetAddress.guess(AddrType.IP4).isPresent());
        assertTrue(LocalInetAddress.guess(AddrType.IP6).isPresent());
        assertNotEquals(LocalInetAddress.guess(AddrType.IP4).get().toString(),
                LocalInetAddress.guess(AddrType.IP6).get().toString());        
    }

}
