package com.zoomulus.speakeasy.sdp.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import org.junit.Test;

import com.zoomulus.speakeasy.core.types.AddrType;
import com.zoomulus.speakeasy.core.types.LocalInetAddress;
import com.zoomulus.speakeasy.core.types.NetType;

public class TestSDPOrigin
{
    SDPUsername emptyUsername = new SDPUsername();
    SDPUsername bobUsername = new SDPUsername("bob");
    SDPNumericId sessId = new SDPNumericId();
    SDPNumericId sessVersion = new SDPNumericId();
    InetAddress localIP4Addr = LocalInetAddress.guess(AddrType.IP4).get();
    InetAddress localIP6Addr = LocalInetAddress.guess(AddrType.IP6).get();
    
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
        assertEquals(expected, new SDPOrigin().toString());
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
    public void testParseDefaultString() throws UnknownHostException
    {
        final Optional<SDPOrigin> origin = SDPOrigin.fromString("- 12345 23456 IN IP4 100.101.102.103");
        assertTrue(origin.isPresent());
        assertEquals(new SDPNumericId("12345"), origin.get().sessId());
        assertEquals(new SDPNumericId("23456"), origin.get().sessVersion());
        validate(origin.get(),
                emptyUsername,
                AddrType.IP4,
                Inet4Address.getByName("100.101.102.103"));
        
    }
    
    @Test
    public void testParseCustomString() throws UnknownHostException
    {
        final Optional<SDPOrigin> origin = SDPOrigin.fromString("bob 12345 23456 IN IP4 100.101.102.103");
        assertTrue(origin.isPresent());
        assertEquals(new SDPNumericId("12345"), origin.get().sessId());
        assertEquals(new SDPNumericId("23456"), origin.get().sessVersion());
        validate(origin.get(),
                bobUsername,
                AddrType.IP4,
                Inet4Address.getByName("100.101.102.103"));
    }
    
    @Test
    public void testParseIP6Addr() throws UnknownHostException
    {
        final Optional<SDPOrigin> origin = SDPOrigin.fromString("bob 12345 23456 IN IP6 fe80::3e07:54ff:fe46:8e6a");
        assertTrue(origin.isPresent());
        assertEquals(new SDPNumericId("12345"), origin.get().sessId());
        assertEquals(new SDPNumericId("23456"), origin.get().sessVersion());
        validate(origin.get(),
                bobUsername,
                AddrType.IP6,
                Inet6Address.getByName("fe80::3e07:54ff:fe46:8e6a"));
    }
    
    @Test
    public void testOriginWithDNSWorks() throws UnknownHostException
    {
        final Optional<SDPOrigin> origin = SDPOrigin.fromString("bob 12345 23456 IN IP4 www.jive.com");
        assertTrue(origin.isPresent());
        assertEquals(new SDPNumericId("12345"), origin.get().sessId());
        assertEquals(new SDPNumericId("23456"), origin.get().sessVersion());
        validate(origin.get(),
                bobUsername,
                AddrType.IP4,
                Inet6Address.getByName("www.jive.com"));   
    }
    
    @Test
    public void testMismatchBetweenAddrTypeAndUnicastAddressFails()
    {
        assertFalse(SDPOrigin.fromString("bob 12345 23456 IN IP6 100.101.102.103").isPresent());
    }
    
    @Test
    public void testParseIrregularlyFormattedString() throws UnknownHostException
    {
        final Optional<SDPOrigin> origin = SDPOrigin.fromString(" bob  12345 23456      IN   IP4  100.101.102.103             ");
        assertTrue(origin.isPresent());
        assertEquals(new SDPNumericId("12345"), origin.get().sessId());
        assertEquals(new SDPNumericId("23456"), origin.get().sessVersion());
        validate(origin.get(),
                bobUsername,
                AddrType.IP4,
                Inet4Address.getByName("100.101.102.103"));        
    }
    
    @Test
    public void testInvalidSessionIdThrowsNumberFormatException()
    {
        assertFalse(SDPOrigin.fromString("bob 12e45 23456 IN IP4 100.101.102.103").isPresent());
    }
    
    @Test
    public void testInvalidSessionVersionThrowsNumberFormatException()
    {
        assertFalse(SDPOrigin.fromString("bob 12345 23f56 IN IP4 100.101.102.103").isPresent());
    }
    
    @Test
    public void testInvalidNetTypeThrowsIllegalArgumentException()
    {
        assertFalse(SDPOrigin.fromString("bob 12345 23456 OUT IP4 100.101.102.103").isPresent());
    }
    
    @Test
    public void testInvalidAddrTypeThrowsIllegalArgumentException()
    {
        assertFalse(SDPOrigin.fromString("bob 12345 23456 IN IP5 100.101.102.103").isPresent());
    }
    
    @Test
    public void testInvalidInetAddrThrowsUnknownHostException()
    {
        final Optional<SDPOrigin> origin = SDPOrigin.fromString("bob 12345 23456 IN IP6 100.200.300.400");
        assertFalse(SDPOrigin.fromString("bob 12345 23456 IN IP6 100.200.300.400").isPresent());
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
