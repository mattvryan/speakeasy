package com.zoomulus.speakeasy.core.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TestEmailAddress
{
    @Test
    public void testConstructByParts()
    {
        EmailAddress ea = new EmailAddress(new Word("test"), new Domain("zoomulus.com"));
        assertEquals("test@zoomulus.com", ea.toString());
    }
    
    @Test
    public void testConstructWithString()
    {
        EmailAddress ea = new EmailAddress("test@zoomulus.com");
        assertEquals("test@zoomulus.com", ea.toString());
    }
    
    @Test
    public void testParseString()
    {
        EmailAddress ea = EmailAddress.parseString("test@zoomulus.com");
        assertEquals("test@zoomulus.com", ea.toString());
    }
    
    @Test
    public void testConstructWithInvalidStringsThrowsIllegalArgumentException()
    {
        ImmutableList<String> invalidEmails = new ImmutableList.Builder<String>()
                .add("test@nodot")
                .add("test@too.many.dots")
                .add("test@domain with.spaces")
                .add("test@tld.with spaces")
                .add("")
                .add("no.email.separator")
                .build();
        for (String email : invalidEmails)
        {
            try
            {
                new EmailAddress(email);
                fail(email);
            }
            catch (IllegalArgumentException e)
            { }
        }
    }
    
    @Test
    public void testEquals()
    {
        EmailAddress ea1 = new EmailAddress(new Word("test"), new Domain("zoomulus.com"));
        EmailAddress ea2 = new EmailAddress("test@zoomulus.com");
        assertEquals(ea1, ea2);
    }
}
