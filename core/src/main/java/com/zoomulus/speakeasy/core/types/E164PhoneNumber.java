package com.zoomulus.speakeasy.core.types;

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
    private String srep = null;
    
    public E164PhoneNumber(@NonNull final NumericString countryCode, @NonNull final NumericString nationalNumber)
    {
        this.countryCode = countryCode;
        this.nationalNumber = nationalNumber;
    }
    
    public E164PhoneNumber(@NonNull final String countryCode, @NonNull final String nationalNumber)
    {
        this.countryCode = new NumericString(countryCode);
        this.nationalNumber = new NumericString(nationalNumber);
    }
    
    @Override
    public String toString()
    {
        if (null == srep)
        {
            srep = "+" + countryCode + " " + nationalNumber;
        }
        return srep;
    }
}
