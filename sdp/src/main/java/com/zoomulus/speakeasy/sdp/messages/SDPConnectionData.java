package com.zoomulus.speakeasy.sdp.messages;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Optional;

import lombok.Getter;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.types.AddrType;
import com.zoomulus.speakeasy.core.types.NetType;

public class SDPConnectionData
{
    @Getter
    @Accessors(fluent=true)
    private final NetType netType = NetType.IN;
    @Getter
    @Accessors(fluent=true)
    private final AddrType addrType;
    @Getter
    @Accessors(fluent=true)
    private final InetAddress connectionAddress;
    @Getter
    @Accessors(fluent=true)
    private final Optional<Integer> ttl;
    @Getter
    @Accessors(fluent=true)
    private final Optional<Integer> numberMulticastAddresses;
    private String srep;
    
    public SDPConnectionData(final Inet4Address connectionAddress)
    {
        if (connectionAddress.isMulticastAddress())
        {
            throw new IllegalArgumentException("Must specify ttl with multicast address");
        }
        this.connectionAddress = connectionAddress;
        ttl = Optional.<Integer> empty();
        numberMulticastAddresses = Optional.of(1);
        addrType = AddrType.IP4;
    }
    
    public SDPConnectionData(final Inet4Address connectionAddress, final int ttl)
    {
        this(connectionAddress, ttl, 1);
    }
    
    public SDPConnectionData(final Inet4Address connectionAddress, final int ttl, final int nMulticastAddresses)
    {
        if (0 > ttl || 255 < ttl)
        {
            throw new IllegalArgumentException(String.format("Invalid ttl: %d", ttl));
        }
        if (! connectionAddress.isMulticastAddress())
        {
            throw new IllegalArgumentException(String.format("Addr %s is not multicast", connectionAddress.toString()));
        }
        this.connectionAddress = connectionAddress;
        this.ttl = Optional.of(ttl);
        this.numberMulticastAddresses = Optional.of(nMulticastAddresses);
        addrType = AddrType.IP4;        
    }
    
    public SDPConnectionData(final Inet6Address connectionAddress)
    {
        this(connectionAddress, 1);
    }
    
    public SDPConnectionData(final Inet6Address connectionAddress, final int nMulticastAddresses)
    {
        this.connectionAddress = connectionAddress;
        ttl = Optional.<Integer> empty();
        numberMulticastAddresses = Optional.of(nMulticastAddresses);
        addrType = AddrType.IP6;        
    }
    
    public String toString()
    {
        if (null == srep)
        {
            if (AddrType.IP4 == addrType)
            {
                if (connectionAddress.isMulticastAddress())
                {
                    if (numberMulticastAddresses.isPresent() &&
                            1 < numberMulticastAddresses.get().intValue())
                    {
                        srep = String.format("%s %s %s/%d/%d",
                                netType, addrType,
                                connectionAddress,
                                ttl.get().intValue(),
                                numberMulticastAddresses.get().intValue());
                    }
                    else
                    {
                        srep = String.format("%s %s %s/%d",
                                netType, addrType,
                                connectionAddress,
                                ttl.get().intValue());
                    }
                }
                else
                {
                    srep = String.format("%s %s %s", netType, addrType, connectionAddress);
                }
            }
            else
            {
                if (connectionAddress.isMulticastAddress() &&
                        numberMulticastAddresses.isPresent() &&
                        1 < numberMulticastAddresses.get().intValue())
                {
                    srep = String.format("%s %s %s/%d",
                            netType, addrType,
                            connectionAddress,
                            numberMulticastAddresses.get().intValue());
                }
                else
                {
                    srep = String.format("%s %s %s", netType, addrType, connectionAddress);
                }
            }
        }
        return srep;
    }
}
