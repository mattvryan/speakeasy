package com.zoomulus.speakeasy.core.util;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

import com.google.common.collect.Maps;

public class LocalInetAddress
{
    private static final String IPV4_BROADCAST_ADDR = "255.255.255.255";
    
    public static Optional<InetAddress> guess(final AddrType addrType)
    {
        Enumeration<NetworkInterface> ifcs;
        
        try
        {
            ifcs = NetworkInterface.getNetworkInterfaces();
        }
        catch (SocketException e)
        {
            return Optional.<InetAddress> empty();
        }
        
        Map<String, Optional<InetAddress>> normalAddrs = Maps.newHashMap();
        Map<String, Optional<InetAddress>> siteLocalAddrs = Maps.newHashMap();
        Map<String, Optional<InetAddress>> linkLocalAddrs = Maps.newHashMap();
        
        while (ifcs.hasMoreElements())
        {
            NetworkInterface ifc = ifcs.nextElement();
            if (ifc.isVirtual()) continue;
            try
            {
                if (ifc.isLoopback()) continue;
                if (! ifc.isUp()) continue;
            }
            catch (SocketException e) { }
            
            if (ifc.getName().startsWith("en") || ifc.getName().startsWith("eth"))
            {
                Enumeration<InetAddress> addrs = ifc.getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    InetAddress addr = addrs.nextElement();
                    if ( (addrType == AddrType.IP4 && addr instanceof Inet6Address) ||
                            (addrType == AddrType.IP6 && addr instanceof Inet4Address) ) continue;
                    if (addr.isLoopbackAddress()) continue;
                    else if (addr.isMulticastAddress()) continue;
                    else if (IPV4_BROADCAST_ADDR.equals(addr.getHostAddress())) continue;
                    else if (addr.isSiteLocalAddress() &&
                            ! siteLocalAddrs.containsKey(ifc.getName()))
                    {
                        siteLocalAddrs.put(ifc.getName(), Optional.of(addr));
                    }
                    else if (addr.isLinkLocalAddress() &&
                            ! linkLocalAddrs.containsKey(ifc.getName()))
                    {
                        linkLocalAddrs.put(ifc.getName(), Optional.of(addr));
                    }
                    else if (! normalAddrs.containsKey(ifc.getName()))
                    {
                        normalAddrs.put(ifc.getName(), Optional.of(addr));
                    }
                }
            }
        }
        
        if (! normalAddrs.isEmpty())
        {
            return normalAddrs.get(new TreeSet<String>(normalAddrs.keySet()).first());
        }
        else if (! siteLocalAddrs.isEmpty())
        {
            return siteLocalAddrs.get(new TreeSet<String>(siteLocalAddrs.keySet()).first());
        }
        else if (! linkLocalAddrs.isEmpty())
        {
            return linkLocalAddrs.get(new TreeSet<String>(linkLocalAddrs.keySet()).first());
        }
        return Optional.<InetAddress> empty();
    }
}
