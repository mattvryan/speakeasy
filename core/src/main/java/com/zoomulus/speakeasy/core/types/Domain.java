package com.zoomulus.speakeasy.core.types;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public class Domain
{
    private final Word name;
    private final Word tld;
    private String srep = null;
    
    public Domain(@NonNull final Word name, @NonNull final Word tld)
    {
        this.name = name;
        this.tld = tld;
    }
    
    public Domain(@NonNull final String srep)
    {
        String[] parts = srep.split("\\.");
        if (2 == parts.length)
        {
            name = new Word(parts[0]);
            tld = new Word(parts[1]);
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
