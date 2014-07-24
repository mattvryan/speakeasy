package com.zoomulus.speakeasy.core.types;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zoomulus.speakeasy.core.types.AddrType;
import com.zoomulus.speakeasy.core.types.LocalInetAddress;

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
