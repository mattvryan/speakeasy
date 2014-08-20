package com.zoomulus.speakeasy.core.types;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * A NumericString is a string comprised only of decimal values.
 */
@EqualsAndHashCode
public class NumericString
{
    private final String val;
    
    public NumericString(@NonNull final String val)
    {
        // The session id must be numeric.  Converting it to a long ensures
        // that the value is valid for a session id.  A NumberFormatException
        // will be thrown if the provided session id is invalid.
        Long.parseUnsignedLong(val);
        this.val = val;
    }
    
    public String toString()
    {
        return val;
    }
}
