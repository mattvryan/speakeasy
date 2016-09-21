package com.zoomulus.speakeasy.sdp.types;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.zoomulus.speakeasy.core.types.AddrType;
import com.zoomulus.speakeasy.core.types.LocalInetAddress;
import com.zoomulus.speakeasy.core.types.NetType;
import com.zoomulus.speakeasy.sdp.types.SDPConnectionData;

public class TestSDPConnectionData
{
    Optional<InetAddress> local4;
    Optional<InetAddress> local6;
    InetAddress ip4Multi = null;
    InetAddress ip6Multi = null;
    
    @Before
    public void setup()
    {
        // If we don't have a working network, skip these tests
        assumeTrue(LocalInetAddress.guess(AddrType.IP4).isPresent());
        
        local4 = LocalInetAddress.guess(AddrType.IP4);
        local6 = LocalInetAddress.guess(AddrType.IP6);
        
        try
        {
            ip4Multi = Inet4Address.getByName("224.0.0.0");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        try
        {
            ip6Multi = Inet6Address.getByName("ff02::1");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }        
    }
    
    @Test
    public void testConstructIP4()
    {
        SDPConnectionData connData = new SDPConnectionData((Inet4Address) local4.get());
        assertEquals(NetType.IN, connData.netType());
        assertEquals(AddrType.IP4, connData.addrType());
        assertEquals(local4.get(), connData.connectionAddress());
        assertFalse(connData.ttl().isPresent());
        assertTrue(connData.connectionAddress() instanceof Inet4Address);
        assertEquals(String.format("IN IP4 %s", local4.get().toString()), connData.toString());        
    }
    
    @Test
    public void testConstructIP4WithTTL()
    {
        SDPConnectionData connData = new SDPConnectionData((Inet4Address) ip4Multi, 250);
        assertEquals(NetType.IN, connData.netType());
        assertEquals(AddrType.IP4, connData.addrType());
        assertEquals(ip4Multi, connData.connectionAddress());
        assertEquals(250, connData.ttl().get().intValue());
        assertTrue(connData.connectionAddress() instanceof Inet4Address);
        assertEquals(String.format("IN IP4 %s/250", ip4Multi.toString()), connData.toString());
    }
    
    @Test
    public void testConstructIP6()
    {
        SDPConnectionData connData = new SDPConnectionData((Inet6Address) local6.get());
        assertEquals(NetType.IN, connData.netType());
        assertEquals(AddrType.IP6, connData.addrType());
        assertEquals(local6.get(), connData.connectionAddress());
        assertFalse(connData.ttl().isPresent());
        assertTrue(connData.connectionAddress() instanceof Inet6Address);
        assertEquals(String.format("IN IP6 %s", local6.get().toString()), connData.toString());        
    }
    
    @Test
    public void testConstructFailsWithInvalidTTL()
    {
        try
        {
            new SDPConnectionData((Inet4Address) local4.get(), 256);
            fail("256");
        }
        catch (IllegalArgumentException e)
        { }
        try
        {
            new SDPConnectionData((Inet4Address) local4.get(), 1000);
            fail("1000");
        }
        catch (IllegalArgumentException e)
        { }
        try
        {
            new SDPConnectionData((Inet4Address) local4.get(), -1);
            fail("-1");
        }
        catch (IllegalArgumentException e)
        { }
    }
    
    @Test
    public void testConstructIP4Multicast()
    {
        SDPConnectionData connData = new SDPConnectionData((Inet4Address) ip4Multi, 100, 4);
        assertEquals(NetType.IN, connData.netType());
        assertEquals(AddrType.IP4, connData.addrType());
        assertEquals(ip4Multi, connData.connectionAddress());
        assertEquals(100, connData.ttl().get().intValue());
        assertTrue(connData.connectionAddress() instanceof Inet4Address);
        assertTrue(connData.connectionAddress().isMulticastAddress());
        assertEquals(String.format("IN IP4 %s/100/4", ip4Multi.toString()), connData.toString());        
    }
    
    @Test
    public void testConstructIP6Multicast()
    {
        SDPConnectionData connData = new SDPConnectionData((Inet6Address) ip6Multi, 4);
        assertEquals(NetType.IN, connData.netType());
        assertEquals(AddrType.IP6, connData.addrType());
        assertEquals(ip6Multi, connData.connectionAddress());
        assertFalse(connData.ttl().isPresent());
        assertTrue(connData.connectionAddress() instanceof Inet6Address);
        assertTrue(connData.connectionAddress().isMulticastAddress());
        assertEquals(String.format("IN IP6 %s/4", ip6Multi.toString()), connData.toString());        
    }
}
