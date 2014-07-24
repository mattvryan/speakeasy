package com.zoomulus.speakeasy.core.util;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import com.google.common.base.Strings;


/**
 * A Word is a String that doesn't have any whitespace inside it.
 */
@EqualsAndHashCode
public class Word
{
    private final String word;
    
    public Word(@NonNull final String word)
    {
        if (Strings.isNullOrEmpty(word))
        {
            throw new IllegalArgumentException("Content must be a non-empty string");
        }
        else if (word.contains(" "))
        {
            throw new IllegalArgumentException("Usernames cannot contain spaces");
        }
        this.word = word;
    }
    
    @Override
    public String toString()
    {
        return word;
    }
}
