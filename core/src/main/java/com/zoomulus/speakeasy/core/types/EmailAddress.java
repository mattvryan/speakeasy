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
    private final boolean nameComesFirst;
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
        nameComesFirst = false;
    }
    
    public EmailAddress(@NonNull final String srep)
    {
        String[] parts = srep.split("@");
        if (2 == parts.length)
        {
            String[] part1parts = parts[0].split("<");
            if (1 < part1parts.length)
            {
                // Address is in the format "common name <email>"
                name = Optional.of(part1parts[0].trim());
                user = new Word(part1parts[1].trim());
                domain = new Domain(parts[1].split(">")[0].trim());
                nameComesFirst = true;
                return;
            }
            
            String[] part2parts = parts[1].split("\\(");
            if (1 < part2parts.length)
            {
                // Address is in the format "email (common name)"
                user = new Word(parts[0].trim());
                domain = new Domain(part2parts[0].trim());
                name = Optional.of(part2parts[1].split("\\)")[0].trim());
                nameComesFirst = false;
                return;
            }
            
            user = new Word(parts[0].trim());
            domain = new Domain(parts[1].trim());
            name = Optional.<String> empty();
            nameComesFirst = false;
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
            String emailAddr = new StringBuilder()
                .append(user)
                .append("@")
                .append(domain)
                .toString();
            
            if (nameComesFirst)
            {
                if (name.isPresent())
                {
                    srep = new StringBuilder()
                        .append(name.get())
                        .append(" <")
                        .append(emailAddr)
                        .append(">")
                        .toString();
                }
                else
                {
                    srep = emailAddr;
                }
            }
            else
            {
                if (name.isPresent())
                {
                    srep = new StringBuilder()
                        .append(emailAddr)
                        .append(" (")
                        .append(name.get())
                        .append(")")
                        .toString();
                }
                else
                {
                    srep = emailAddr;
                }
            }
        }
        return srep;
    }
}
