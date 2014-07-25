package com.zoomulus.speakeasy.core.types;

import java.util.Optional;

import com.google.common.base.Strings;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode
public class E164PhoneNumber
{
    @Getter
    private final NumericString countryCode;
    @Getter
    private final NumericString nationalNumber;
    private final Optional<String> name;
    private final boolean nameComesFirst;
    private String srep = null;
    
    public E164PhoneNumber(@NonNull final NumericString countryCode,
            @NonNull final NumericString nationalNumber)
    {
        this(countryCode, nationalNumber, null);
    }
    
    public E164PhoneNumber(@NonNull final NumericString countryCode,
            @NonNull final NumericString nationalNumber,
            final String name)
    {
        this.countryCode = countryCode;
        this.nationalNumber = nationalNumber;
        this.name = Strings.isNullOrEmpty(name) ? Optional.<String> empty() : Optional.of(name);
        nameComesFirst = false;
    }
    
    public E164PhoneNumber(@NonNull final String countryCode,
            @NonNull final String nationalNumber)
    {
        this(countryCode, nationalNumber, null);
    }
    
    public E164PhoneNumber(@NonNull final String countryCode,
            @NonNull final String nationalNumber,
            final String name)
    {
        this(new NumericString(countryCode), new NumericString(nationalNumber), name);
    }
    
    public E164PhoneNumber(@NonNull final String phoneNumber)
    {
        if (phoneNumber.trim().startsWith("+"))
        {
            String[] parts = phoneNumber.split("\\(");
            String[] part1parts = parts[0].split(" ");
            if (2 > part1parts.length)
            {
                throw new IllegalArgumentException("Malformed phone number");
            }
            countryCode = new NumericString(part1parts[0].trim().substring(1));
            nationalNumber = new NumericString(part1parts[1].trim());
            if (1 < parts.length)
            {
                name = Optional.of(parts[1].split("\\)")[0].trim());
            }
            else
            {
                name = Optional.<String> empty();
            }
            nameComesFirst = false;
        }
        else
        {
            String[] parts = phoneNumber.split("<");
            if (1 < parts.length)
            {
                if (! parts[1].endsWith(">"))
                {
                    throw new IllegalArgumentException("Malformed phone number");
                }
                name = Optional.of(parts[0].trim());
                String[] part2parts = parts[1].split(" ");
                countryCode = new NumericString(part2parts[0].trim().substring(1));
                nationalNumber = new NumericString(part2parts[1].split(">")[0].trim());
            }
            else
            {
                name = Optional.<String> empty();
                String[] part1parts = parts[1].split(" ");
                countryCode = new NumericString(part1parts[0].trim().substring(1));
                nationalNumber = new NumericString(part1parts[1].trim());
            }
            nameComesFirst = true;
        }
    }
    
    @Override
    public String toString()
    {
        if (null == srep)
        {
            String phoneNumber = new StringBuilder()
                .append("+")
                .append(countryCode)
                .append(" ")
                .append(nationalNumber)
                .toString();
            
            if (nameComesFirst)
            {
                if (name.isPresent())
                {
                    srep = new StringBuilder()
                        .append(name.get())
                        .append(" <")
                        .append(phoneNumber)
                        .append(">")
                        .toString();
                }
                else
                {
                    srep = phoneNumber;
                }
            }
            else
            {
                if (name.isPresent())
                {
                    srep = new StringBuilder()
                        .append(phoneNumber)
                        .append(" (")
                        .append(name.get())
                        .append(")")
                        .toString();
                }
                else
                {
                    srep = phoneNumber;
                }
            }
        }
        return srep;
    }
}
