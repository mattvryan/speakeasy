package com.zoomulus.speakeasy.core.types;

import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import com.google.common.base.Strings;

@EqualsAndHashCode
public class EmailAddress
{
    private final Word user;
    private final Domain domain;
    private final Optional<String> name;
    private String srep = null;
    
    public EmailAddress(@NonNull final Word user, @NonNull final Domain domain)
    {
        this(user, domain, null);
    }
    public EmailAddress(@NonNull final Word user, @NonNull final Domain domain, final String name)
    {
        this.user = user;
        this.domain = domain;
        this.name = Strings.isNullOrEmpty(name) ? Optional.<String> empty() : Optional.of(name);
    }
    
    public EmailAddress(@NonNull final String srep)
    {
        String[] parts = srep.split("@");
        if (2 == parts.length)
        {
            user = new Word(parts[0]);
            domain = new Domain(parts[1]);
            name = Optional.<String> empty();
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
            srep = user + "@" + domain + (name.isPresent() ? (" (" + name.get() + ")") : "");
        }
        return srep;
    }
}
