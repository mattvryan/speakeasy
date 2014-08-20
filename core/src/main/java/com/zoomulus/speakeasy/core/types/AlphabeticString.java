package com.zoomulus.speakeasy.core.types;

import lombok.EqualsAndHashCode;

/**
 * An AlphabeticString is a Word that is comprised only of alphanumeric characters.
 */
@EqualsAndHashCode(callSuper=true)
public class AlphabeticString extends Word
{
    public AlphabeticString(String as)
    {
        super(as);
        for (char c : as.toCharArray())
        {
            if (! Character.isLetter(c))
            {
                throw new IllegalArgumentException("Cannot create an AlphabeticString that contains non-alphabetic characters");
            }
        }
    }
}
