package com.zoomulus.speakeasy.core.types;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode
public class EmailAddress
{
    private final Word user;
    private final Domain domain;
    private String srep = null;
    
    public EmailAddress(@NonNull final Word user, @NonNull final Domain domain)
    {
        this.user = user;
        this.domain = domain;
    }
    
    public EmailAddress(@NonNull final String srep)
    {
        String[] parts = srep.split("@");
        if (2 == parts.length)
        {
            user = new Word(parts[0]);
            domain = new Domain(parts[1]);
        }
        else
        {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
    
    public static EmailAddress parseString(@NonNull final String srep)
    {
        return new EmailAddress(srep);
    }
    
    @Override
    public String toString()
    {
        if (null == srep)
        {
            srep = user + "@" + domain;
        }
        return srep;
    }
}
