package com.zoomulus.speakeasy.core.types;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestE164PhoneNumber
{
    @Test
    public void testConstructWithNumericStrings()
    {
        E164PhoneNumber pn = new E164PhoneNumber(new NumericString("1"), new NumericString("1235554567"));
        assertEquals("1", pn.getCountryCode().toString());
        assertEquals("1235554567", pn.getNationalNumber().toString());
        assertEquals("+1 1235554567", pn.toString());
    }
    
    @Test
    public void testConstructWithStrings()
    {
        E164PhoneNumber pn = new E164PhoneNumber("1", "1235554567");
        assertEquals("1", pn.getCountryCode().toString());
        assertEquals("1235554567", pn.getNationalNumber().toString());
        assertEquals("+1 1235554567", pn.toString());
    }
    
    @Test
    public void testConstructWithName()
    {
        E164PhoneNumber pn = new E164PhoneNumber("1", "1235554567", "Test User");
        assertEquals("1", pn.getCountryCode().toString());
        assertEquals("1235554567", pn.getNationalNumber().toString());
        assertEquals("+1 1235554567 (Test User)", pn.toString());                
    }
    
    @Test
    public void testConstructWithSingleString()
    {
        E164PhoneNumber pn = new E164PhoneNumber("+1 1235554567");
        assertEquals("1", pn.getCountryCode().toString());
        assertEquals("1235554567", pn.getNationalNumber().toString());
        assertEquals("+1 1235554567", pn.toString());        
    }
    
    @Test
    public void testConstructWithLeadingName()
    {
        E164PhoneNumber pn = new E164PhoneNumber("Test User <+1 1235554567>");
        assertEquals("1", pn.getCountryCode().toString());
        assertEquals("1235554567", pn.getNationalNumber().toString());
        assertEquals("Test User <+1 1235554567>", pn.toString());        
    }
    
    @Test
    public void testConstructWithTrailingName()
    {
        E164PhoneNumber pn = new E164PhoneNumber("+1 1235554567 (Test User)");
        assertEquals("1", pn.getCountryCode().toString());
        assertEquals("1235554567", pn.getNationalNumber().toString());
        assertEquals("+1 1235554567 (Test User)", pn.toString());        
    }
}
