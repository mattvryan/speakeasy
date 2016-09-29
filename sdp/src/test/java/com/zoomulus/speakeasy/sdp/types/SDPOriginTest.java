package com.zoomulus.speakeasy.sdp.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.zoomulus.speakeasy.core.types.AddrType;
import com.zoomulus.speakeasy.core.types.LocalInetAddress;
import com.zoomulus.speakeasy.core.types.NetType;
import com.zoomulus.speakeasy.sdp.messages.exceptions.SDPParseException;

public class SDPOriginTest
{
    SDPUsername emptyUsername = new SDPUsername();
    SDPUsername bobUsername = new SDPUsername("bob");
    SDPNumericId sessId = new SDPNumericId();
    SDPNumericId sessVersion = new SDPNumericId();
    InetAddress localIP4Addr;
    InetAddress localIP6Addr;
    
    private void validate(final SDPOrigin o, final SDPUsername username, final AddrType addrType, final InetAddress address)
    {
        assertNotNull(o);
        assertEquals(username, o.username());
        assertNotNull(o.sessId());
        assertNotNull(o.sessVersion());
        assertEquals(NetType.IN, o.netType());
        assertEquals(addrType, o.addrType());
        assertEquals(address, o.unicastAddress());
        if (AddrType.IP4 == o.addrType())
        {
            assertTrue(o.unicastAddress() instanceof Inet4Address);
        }
        else
        {
            assertTrue(o.unicastAddress() instanceof Inet6Address);
        }
    }
    
    @Before
    public void setup()
    {
        // If we don't have a working network, skip these tests
        assumeTrue(LocalInetAddress.guess(AddrType.IP4).isPresent());
        
        localIP4Addr = LocalInetAddress.guess(AddrType.IP4).get();
        localIP6Addr = LocalInetAddress.guess(AddrType.IP6).get();
    }
    
    @Test
    public void testDefaultSDPOriginToString()
    {
        final String expected = String.format("%s %s %s %s %s %s",
                emptyUsername,
                sessId,
                sessVersion,
                NetType.IN,
                AddrType.IP4,
                localIP4Addr.getCanonicalHostName());
        assertEquals(expected, new SDPOrigin(sessId, sessVersion).toString());
    }
    
    @Test
    public void testCustomSDPOriginToString()
    {
        final String expected = String.format("%s %s %s %s %s %s",
                bobUsername,
                sessId,
                sessVersion,
                NetType.IN,
                AddrType.IP6,
                localIP6Addr.getCanonicalHostName());
        assertEquals(expected, new SDPOrigin(bobUsername, sessId, sessVersion, localIP6Addr).toString());
    }
    
    @Test
    public void testParseDefaultString() throws UnknownHostException, SDPParseException
    {
        final SDPOrigin origin = SDPOrigin.parse("- 12345 23456 IN IP4 100.101.102.103");
        assertEquals(new SDPNumericId("12345"), origin.sessId());
        assertEquals(new SDPNumericId("23456"), origin.sessVersion());
        validate(origin,
                emptyUsername,
                AddrType.IP4,
                Inet4Address.getByName("100.101.102.103"));
        
    }
    
    @Test
    public void testParseCustomString() throws SDPParseException, UnknownHostException
    {
        final SDPOrigin origin = SDPOrigin.parse("bob 12345 23456 IN IP4 100.101.102.103");
        assertEquals(new SDPNumericId("12345"), origin.sessId());
        assertEquals(new SDPNumericId("23456"), origin.sessVersion());
        validate(origin,
                bobUsername,
                AddrType.IP4,
                Inet4Address.getByName("100.101.102.103"));
    }
    
    @Test
    public void testParseIP6Addr() throws SDPParseException, UnknownHostException
    {
        final SDPOrigin origin = SDPOrigin.parse("bob 12345 23456 IN IP6 fe80::3e07:54ff:fe46:8e6a");
        assertEquals(new SDPNumericId("12345"), origin.sessId());
        assertEquals(new SDPNumericId("23456"), origin.sessVersion());
        validate(origin,
                bobUsername,
                AddrType.IP6,
                Inet6Address.getByName("fe80::3e07:54ff:fe46:8e6a"));
    }
    
    @Test
    public void testOriginWithDNSWorks() throws SDPParseException, UnknownHostException
    {
        final SDPOrigin origin = SDPOrigin.parse("bob 12345 23456 IN IP4 www.zoomulus.com");
        assertEquals(new SDPNumericId("12345"), origin.sessId());
        assertEquals(new SDPNumericId("23456"), origin.sessVersion());
        validate(origin,
                bobUsername,
                AddrType.IP4,
                Inet6Address.getByName("www.zoomulus.com"));   
    }
    
    @Test
    public void testMismatchBetweenAddrTypeAndUnicastAddressFails()
    {
        try
        {
            SDPOrigin.parse("bob 12345 23456 IN IP6 100.101.102.103");
            fail();
        }
        catch (SDPParseException e)
        {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        } 
    }
    
    @Test
    public void testParseIrregularlyFormattedString() throws SDPParseException, UnknownHostException
    {
        final SDPOrigin origin = SDPOrigin.parse(" bob  12345 23456      IN   IP4  100.101.102.103             ");
        assertEquals(new SDPNumericId("12345"), origin.sessId());
        assertEquals(new SDPNumericId("23456"), origin.sessVersion());
        validate(origin,
                bobUsername,
                AddrType.IP4,
                Inet4Address.getByName("100.101.102.103"));        
    }
    
    @Test
    public void testInvalidSessionIdThrowsNumberFormatException()
    {
        try
        {
            SDPOrigin.parse("bob 12e45 23456 IN IP4 100.101.102.103");
            fail();
        }
        catch (SDPParseException e)
        {
            assertTrue(e.getCause() instanceof NumberFormatException);
        }
    }
    
    @Test
    public void testInvalidSessionVersionThrowsNumberFormatException()
    {
        try
        {
            SDPOrigin.parse("bob 12345 23f56 IN IP4 100.101.102.103");
            fail();
        }
        catch (SDPParseException e)
        {
            assertTrue(e.getCause() instanceof NumberFormatException);
        }
    }
    
    @Test
    public void testInvalidNetTypeThrowsIllegalArgumentException()
    {
        try
        {
            SDPOrigin.parse("bob 12345 23456 OUT IP4 100.101.102.103");
            fail();
        }
        catch (SDPParseException e)
        {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testInvalidAddrTypeThrowsIllegalArgumentException()
    {
        try
        {
            SDPOrigin.parse("bob 12345 23456 IN IP5 100.101.102.103");
            fail();
        }
        catch (SDPParseException e)
        {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testInvalidInetAddrThrowsUnknownHostException()
    {
        try
        {
            SDPOrigin.parse("bob 12345 23456 IN IP6 100.200.300.400");
            fail();
        }
        catch (SDPParseException e)
        {
            assertTrue(e.getCause() instanceof UnknownHostException);
        }
    }

    @Test
    public void testEmptyConstructor()
    {
        validate(new SDPOrigin(), emptyUsername, AddrType.IP4, localIP4Addr);
    }
    
    @Test
    public void testSessIdConstructor()
    {
        validate(new SDPOrigin(sessId), emptyUsername, AddrType.IP4, localIP4Addr);
    }
    
    @Test
    public void testSessIdSessVersionConstructor()
    {
        validate(new SDPOrigin(sessId, sessVersion), emptyUsername, AddrType.IP4, localIP4Addr);
    }
    
    @Test
    public void testSessIdSessVersionAddrTypeConstructor()
    {
        validate(new SDPOrigin(sessId, sessVersion, AddrType.IP6), emptyUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testAddrTypeConstructor()
    {
        validate(new SDPOrigin(AddrType.IP6), emptyUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testSessIdAddrTypeConstructor()
    {
        validate(new SDPOrigin(sessId, AddrType.IP6), emptyUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUnicastAddressConstructor()
    {
        validate(new SDPOrigin(localIP4Addr), emptyUsername, AddrType.IP4, localIP4Addr);
        validate(new SDPOrigin(localIP6Addr), emptyUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testSessIdUnicastAddressConstructor()
    {
        validate(new SDPOrigin(sessId, localIP6Addr), emptyUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testSessIdSessVersionUnicastAddressConstructor()
    {
        validate(new SDPOrigin(sessId, sessVersion, localIP6Addr), emptyUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUsernameConstructor()
    {
        validate(new SDPOrigin(bobUsername), bobUsername, AddrType.IP4, localIP4Addr);
    }
    
    @Test
    public void testUsernameSessIdConstructor()
    {
        validate(new SDPOrigin(bobUsername, sessId), bobUsername, AddrType.IP4, localIP4Addr);
    }
    
    @Test
    public void testUsernameSessIdSessVersionConstructor()
    {
        validate(new SDPOrigin(bobUsername, sessId, sessVersion), bobUsername, AddrType.IP4, localIP4Addr);
    }
    
    @Test
    public void testUsernameAddrTypeConstructor()
    {
        validate(new SDPOrigin(bobUsername, AddrType.IP6), bobUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUsernameSessIdAddrTypeConstructor()
    {
        validate(new SDPOrigin(bobUsername, sessId, AddrType.IP6), bobUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUsernameSessIdSessVersionAddrTypeConstructor()
    {
        validate(new SDPOrigin(bobUsername, sessId, sessVersion, AddrType.IP6), bobUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUsernameUnicastAddressConstructor()
    {
        validate(new SDPOrigin(bobUsername, localIP6Addr), bobUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUsernameSessIdUnicastAddressConstructor()
    {
        validate(new SDPOrigin(bobUsername, sessId, localIP6Addr), bobUsername, AddrType.IP6, localIP6Addr);
    }
    
    @Test
    public void testUsernameSessIdSessVersionUnicastAddressConstructor()
    {
        validate(new SDPOrigin(bobUsername, sessId, sessVersion, localIP6Addr), bobUsername, AddrType.IP6, localIP6Addr);
    }
}
