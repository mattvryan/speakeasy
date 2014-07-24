package com.zoomulus.speakeasy.sdp.messages;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.util.AddrType;
import com.zoomulus.speakeasy.core.util.LocalInetAddress;
import com.zoomulus.speakeasy.core.util.NetType;

@Value
@Accessors(fluent=true)
public class SDPOrigin
{
    private final SDPUsername username;
    private final SDPNumericId sessId;
    private final SDPNumericId sessVersion;
    private final NetType netType = NetType.IN;
    private final AddrType addrType;
    private final InetAddress unicastAddress;

    /**
     * Create an SDPOrigin with all default values:
     *  - "-" for the username
     *  - auto-generated sessId and sessVersion
     *  - IP4 addrType
     *  - best-guess local machine's IP4 address
     */
    public SDPOrigin()
    {
        this(new SDPUsername(),
                new SDPNumericId(),
                new SDPNumericId(),
                AddrType.IP4,
                LocalInetAddress.guess(AddrType.IP4).get());
    }

    /**
     * Create an SDPOrigin with the provided session ID.
     * The default username "-" will be used.
     * The sessVersion will be automatically generated.
     * The addrType will default to IP4.
     * A best-guess effort will be used to select the local machine's
     * IP4 address.
     * @param sessionId
     */
    public SDPOrigin(final SDPNumericId sessionId)
    {
        this(new SDPUsername(),
                sessionId,
                new SDPNumericId(),
                AddrType.IP4,
                LocalInetAddress.guess(AddrType.IP4).get());

    }

    /**
     * Create an SDPOrigin with the provided session ID and session version.
     * The default username "-" will be used.
     * The addrType will default to IP4.
     * A best-guess effort will be used to select the local machine's
     * IP4 address.
     * @param sessionId
     * @param sessionVersion
     */
    public SDPOrigin(final SDPNumericId sessionId,
            final SDPNumericId sessionVersion)
    {
        this(new SDPUsername(),
                sessionId,
                sessionVersion,
                AddrType.IP4,
                LocalInetAddress.guess(AddrType.IP4).get());

    }

    /**
     * Create an SDPOrigin with the provided session ID, session version, and addrtype.
     * The default username "-" will be used.
     * A best-guess effort will be used to select the local machine's
     * unicast address based on addrtype.
     * @param sessionId
     * @param sessionVersion
     * @param addrType
     */
    public SDPOrigin(final SDPNumericId sessionId,
            final SDPNumericId sessionVersion,
            final AddrType addrType)
    {
        this(new SDPUsername(),
                sessionId,
                sessionVersion,
                addrType,
                LocalInetAddress.guess(addrType).get());
    }

    /**
     * Create an SDPOrigin with the provided addrtype.
     * The default username "-" will be used.
     * The sessId and sessVersion will be automatically generated.
     * A best-guess effort will be used to select the local machine's
     * unicast address based on addrtype.
     * @param addrType
     */
    public SDPOrigin(final AddrType addrType)
    {
        this(new SDPUsername(),
                new SDPNumericId(),
                new SDPNumericId(),
                addrType,
                LocalInetAddress.guess(addrType).get());        
    }

    /**
     * Create an SDPOrigin with the provided session ID and addrtype.
     * The default username "-" will be used.
     * The sessVersion will be automatically generated.
     * A best-guess effort will be used to select the local machine's
     * unicast address based on addrtype.
     * @param sessionId
     * @param addrType
     */
    public SDPOrigin(final SDPNumericId sessionId,
            final AddrType addrType)
    {
        this(new SDPUsername(),
                sessionId,
                new SDPNumericId(),
                addrType,
                LocalInetAddress.guess(addrType).get());        
    }

    /**
     * Create an SDPOrigin with the provided unicast address.
     * The default username "-" will be used.
     * The sessId and sessVersion will be automatically generated.
     * The addrType will be set automatically based on the unicast address type.
     * @param addrType
     */
    public SDPOrigin(final InetAddress unicastAddress)
    {
        this(new SDPUsername(),
                new SDPNumericId(),
                new SDPNumericId(),
                ((unicastAddress instanceof Inet4Address) ? AddrType.IP4 : AddrType.IP6),
                unicastAddress);
    }

    /**
     * Create an SDPOrigin with the provided session ID and unicast address.
     * The default username "-" will be used.
     * The sessVersion will be automatically generated.
     * The addrType will be set automatically based on the unicast address type.
     * @param sessionId
     * @param addrType
     */
    public SDPOrigin(final SDPNumericId sessionId,
            final InetAddress unicastAddress)
    {
        this(new SDPUsername(),
                sessionId,
                new SDPNumericId(),
                ((unicastAddress instanceof Inet4Address) ? AddrType.IP4 : AddrType.IP6),
                unicastAddress);
    }

    /**
     * Create an SDPOrigin with the provided session ID, session version, and unicast address.
     * The default username "-" will be used.
     * The addrType will be set automatically based on the unicast address type.
     * @param sessionId
     * @param sessionVersion
     * @param addrType
     */
    public SDPOrigin(final SDPNumericId sessionId,
            final SDPNumericId sessionVersion,
            final InetAddress unicastAddress)
    {
        this(new SDPUsername(),
                sessionId,
                sessionVersion,
                ((unicastAddress instanceof Inet4Address) ? AddrType.IP4 : AddrType.IP6),
                unicastAddress);
    }

    /**
     * Create an SDPOrigin with the provided username.
     * The sessId and sessVersion will be automatically generated.
     * The addrType will default to IP4.
     * A best-guess effort will be used to select the local machine's
     * IP4 address.
     * @param username
     */
    public SDPOrigin(final SDPUsername username)
    {
        this(username,
                new SDPNumericId(),
                new SDPNumericId(),
                AddrType.IP4,
                LocalInetAddress.guess(AddrType.IP4).get());
    }

    /**
     * Create an SDPOrigin with the provided username and session ID.
     * The sessVersion will be automatically generated.
     * The addrType will default to IP4.
     * A best-guess effort will be used to select the local machine's
     * IP4 address.
     * @param username
     * @param sessionId
     */
    public SDPOrigin(final SDPUsername username, final SDPNumericId sessionId)
    {
        this(username,
                sessionId,
                new SDPNumericId(),
                AddrType.IP4,
                LocalInetAddress.guess(AddrType.IP4).get());
    }

    /**
     * Create an SDPOrigin with the provided username, session ID, and session version.
     * The addrType will default to IP4.
     * A best-guess effort will be used to select the local machine's
     * IP4 address.
     * @param username
     * @param sessionId
     * @param sessionVersion
     */
    public SDPOrigin(final SDPUsername username,
            final SDPNumericId sessionId,
            final SDPNumericId sessionVersion)
    {
        this(username,
                sessionId,
                sessionVersion,
                AddrType.IP4,
                LocalInetAddress.guess(AddrType.IP4).get());    
    }

    /**
     * Create an SDPOrigin with the provided username and address type.
     * The sessId and sessVersion will be automatically generated.
     * A best-guess effort will be used to select the local machine's
     * address for the provided address type.
     * @param username
     * @param addrType
     */
    public SDPOrigin(final SDPUsername username,
            final AddrType addrType)
    {
        this(username,
                new SDPNumericId(),
                new SDPNumericId(),
                addrType,
                LocalInetAddress.guess(addrType).get());
    }

    /**
     * Create an SDPOrigin with the provided username, session ID, and address type.
     * The sessVersion will be automatically generated.
     * A best-guess effort will be used to select the local machine's
     * address for the provided address type.
     * @param username
     * @param sessionId
     * @param addrType
     */
    public SDPOrigin(final SDPUsername username,
            final SDPNumericId sessionId,
            final AddrType addrType)
    {
        this(username,
                sessionId,
                new SDPNumericId(),
                addrType,
                LocalInetAddress.guess(addrType).get());
    }

    /**
     * Create an SDPOrigin with the provided username, session ID, session version, and address type.
     * A best-guess effort will be used to select the local machine's
     * address for the provided address type.
     * @param username
     * @param sessionId
     * @param sessionVersion
     * @param addrType
     */
    public SDPOrigin(final SDPUsername username,
            final SDPNumericId sessionId,
            final SDPNumericId sessionVersion,
            final AddrType addrType)
    {
        this(username,
                sessionId,
                sessionVersion,
                addrType,
                LocalInetAddress.guess(addrType).get());
    }

    /**
     * Create an SDPOrigin with the provided username and unicast address.
     * The sessId and sessVersion will be automatically generated.
     * The addrType will be set automatically based on the unicast address type.
     * @param username
     * @param unicastAddress
     */
    public SDPOrigin(final SDPUsername username,
            final InetAddress unicastAddress)
    {
        this(username,
                new SDPNumericId(),
                new SDPNumericId(),
                ((unicastAddress instanceof Inet4Address) ? AddrType.IP4 : AddrType.IP6),
                unicastAddress);
    }

    /**
     * Create an SDPOrigin with the provided username, session ID, and unicast address.
     * The sessVersion will be automatically generated.
     * The addrType will be set automatically based on the unicast address type.
     * @param username
     * @param sessionId
     * @param unicastAddress
     */
    public SDPOrigin(final SDPUsername username,
            final SDPNumericId sessionId,
            final InetAddress unicastAddress)
    {
        this(username,
                sessionId,
                new SDPNumericId(),
                ((unicastAddress instanceof Inet4Address) ? AddrType.IP4 : AddrType.IP6),
                unicastAddress);

    }

    /**
     * Create an SDPOrigin with the provided username, session ID, session version, and unicast address.
     * The addrType will be set automatically based on the unicast address type.
     * @param username
     * @param sessionId
     * @param sessionVersion
     * @param unicastAddress
     */
    public SDPOrigin(final SDPUsername username,
            final SDPNumericId sessionId,
            final SDPNumericId sessionVersion,
            final InetAddress unicastAddress)
    {
        this(username,
                sessionId,
                sessionVersion,
                ((unicastAddress instanceof Inet4Address) ? AddrType.IP4 : AddrType.IP6),
                unicastAddress);        
    }

    private SDPOrigin(@NonNull final SDPUsername username,
            @NonNull final SDPNumericId sessionId,
            @NonNull final SDPNumericId sessionVersion,
            @NonNull final AddrType addrType,
            @NonNull final InetAddress unicastAddress)
    {
        this.username = username;
        this.sessId = sessionId;
        this.sessVersion = sessionVersion;
        this.addrType = addrType;
        this.unicastAddress = unicastAddress;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s %s %s %s %s %s",
                username,
                sessId,
                sessVersion,
                netType,
                addrType,
                unicastAddress.getCanonicalHostName());
    }
    
    public static Optional<SDPOrigin> fromString(@NonNull final String stringRep)
    {
        String[] parts = stringRep.trim().split(" +");
        if (parts.length >= 6)
        {
            SDPUsername username = null;
            SDPNumericId sessId = null;
            SDPNumericId sessVersion = null;
            NetType netType = null;
            AddrType addrType = null;
            InetAddress unicastAddress = null;
            
            int ctr = 1;
            
            for (String part : parts)
            {
                String tPart = part.trim();
                if (0 != tPart.length())
                {
                    switch (ctr)
                    {
                        case 1:
                            username = new SDPUsername(tPart);
                            break;
                        case 2:
                            try
                            {
                                sessId = new SDPNumericId(tPart);
                            }
                            catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            try
                            {
                                sessVersion = new SDPNumericId(tPart);
                            }
                            catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case 4:
                            try
                            {
                                netType = NetType.valueOf(tPart);
                            }
                            catch (IllegalArgumentException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case 5:
                            try
                            {
                                addrType = AddrType.valueOf(tPart);
                            }
                            catch (IllegalArgumentException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case 6:
                            try
                            {
                                unicastAddress = InetAddress.getByName(tPart);
                            }
                            catch (UnknownHostException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                if (++ctr > 6) break;
            }
            if (NetType.IN == netType)
            {
                if (((AddrType.IP4 == addrType) && (unicastAddress instanceof Inet4Address)) ||
                        ((AddrType.IP6 == addrType) && (unicastAddress instanceof Inet6Address)))
                {
                    try
                    {
                        return Optional.of(new SDPOrigin(username, sessId, sessVersion, addrType, unicastAddress));
                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    new IllegalArgumentException("Unicast address type does not match provided AddrType")
                    .printStackTrace();
                }
            }
        }
        return Optional.<SDPOrigin> empty();
    }
}
