package com.zoomulus.speakeasy.core.types;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public class Domain
{
    private final AlphabeticString name;
    private final AlphabeticString tld;
    private String srep = null;
    
    public Domain(@NonNull final AlphabeticString name, @NonNull final AlphabeticString tld)
    {
        this.name = name;
        this.tld = tld;
    }
    
    public Domain(@NonNull final String srep)
    {
        String[] parts = srep.split("\\.");
        if (2 == parts.length)
        {
            name = new AlphabeticString(parts[0]);
            tld = new AlphabeticString(parts[1]);
        }
        else
        {
            throw new IllegalArgumentException("Invalid domain");
        }
    }
    
    public static Domain parseString(@NonNull final String srep)
    {
        return new Domain(srep);
    }
    
    @Override
    public String toString()
    {
        if (null == srep)
        {
            srep = name + "." + tld;
        }
        return srep;
    }
}
