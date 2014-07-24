package com.zoomulus.speakeasy.core.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TestDomain
{
    @Test
    public void testConstructWithParts()
    {
        Domain d = new Domain(new AlphabeticString("zoomulus"), new AlphabeticString("com"));
        assertEquals("zoomulus.com", d.toString());
    }
    
    @Test
    public void testConstructWithString()
    {
        Domain d = new Domain("zoomulus.com");
        assertEquals("zoomulus.com", d.toString());
    }
    
    @Test
    public void testParseString()
    {
        Domain d = Domain.parseString("zoomulus.com");
        assertEquals("zoomulus.com", d.toString());
    }
    
    @Test
    public void testConstructWithInvalidDomainsReturnsIllegalArgumentException()
    {
        ImmutableList<String> invalidDomains = new ImmutableList.Builder<String>()
                .add("nodot")
                .add("too.many.dots")
                .add("domain with.spaces")
                .add("tld.with spaces")
                .add("num3r1c.d0ma1n")
                .add("")
                .build();
        for (String invalidDomain : invalidDomains)
        {
            try
            {
                new Domain(invalidDomain);
                fail(invalidDomain);
            }
            catch (IllegalArgumentException e)
            { }
        }
    }
    
    @Test
    public void testEquals()
    {
        Domain d1 = new Domain(new AlphabeticString("zoomulus"), new AlphabeticString("com"));
        Domain d2 = new Domain("zoomulus.com");
        assertEquals(d1, d2);
    }
}
