package com.zoomulus.speakeasy.sdp.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.Instant;
import org.junit.Test;

public class TestSDPTimePeriod
{
    private final long start = 12345 + SDPTimePeriod.UNIX_TO_NTP;
    private final long stop = 23456 + SDPTimePeriod.UNIX_TO_NTP;

    @Test
    public void testToString()
    {
        assertTrue(String.format("%d %d", start, stop).equals(
                new SDPTimePeriod(new Instant(12345 * 1000), new Instant(
                        23456 * 1000)).toString()));
    }

    @Test
    public void testParse()
    {
        final SDPTimePeriod timePeriod = SDPTimePeriod.parse(String.format(
                "%d %d", start, stop));
        assertEquals(start, timePeriod.start());
        assertEquals(stop, timePeriod.stop());
    }

    @Test
    public void testParseWithStopZero()
    {
        final SDPTimePeriod timePeriod = SDPTimePeriod.parse(String.format(
                "%d 0", start));
        assertEquals(start, timePeriod.start());
        assertEquals(0, timePeriod.stop());
    }

    @Test
    public void testParseWithStartZero()
    {
        final SDPTimePeriod timePeriod = SDPTimePeriod.parse("0 0");
        assertEquals(0, timePeriod.start());
        assertEquals(0, timePeriod.stop());
    }

    @Test
    public void testParseWithStartZeroAndNonzeroStopThrowsException()
    {
        try
        {
            SDPTimePeriod.parse("0 1");
            fail("No exception thrown");
        }
        catch (final IllegalArgumentException e)
        {

        }
    }

}
