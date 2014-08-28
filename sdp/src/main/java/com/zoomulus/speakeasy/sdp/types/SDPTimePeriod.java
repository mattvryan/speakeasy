package com.zoomulus.speakeasy.sdp.types;

import java.util.Optional;

import lombok.Getter;
import lombok.experimental.Accessors;

import org.joda.time.Instant;

public class SDPTimePeriod
{
    @Getter
    @Accessors(fluent = true)
    private final long start;
    @Getter
    @Accessors(fluent = true)
    private final long stop;

    public static final long UNIX_TO_NTP = 2208988800L;

    public SDPTimePeriod()
    {
        this(new Instant());
    }

    public SDPTimePeriod(final Instant start)
    {
        this(start, new Instant(0));
    }

    public SDPTimePeriod(final Instant start, final Instant stop)
    {
        this((start.getMillis() / 1000) + UNIX_TO_NTP,
                (stop.getMillis() == 0 ? 0
                        : ((stop.getMillis() / 1000) + UNIX_TO_NTP)));
    }

    private SDPTimePeriod(final long start, final long stop)
    {
        if (0 == start && 0 != stop)
        {
            throw new IllegalArgumentException("Start == 0 invalid with stop != 0");
        }
        this.start = start;
        this.stop = stop;
    }

    @Override
    public String toString()
    {
        return String.format("%d %d", start, stop);
    }

    public static Optional<SDPTimePeriod> tryParse(final String stringrep)
    {
        try
        {
            return Optional.of(SDPTimePeriod.parse(stringrep));
        }
        catch (final IllegalArgumentException e)
        {
        }

        return Optional.<SDPTimePeriod> empty();
    }

    public static SDPTimePeriod parse(final String stringrep)
    {
        final String[] parts = stringrep.split(" ");
        if (2 == parts.length)
        {
            final long start = Long.parseLong(parts[0]);
            final long stop = Long.parseLong(parts[1]);
            return new SDPTimePeriod(start, stop);
        }
        throw new IllegalArgumentException("Invalid format: " + stringrep);
    }
}
