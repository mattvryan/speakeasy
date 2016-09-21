package com.zoomulus.speakeasy.sdp.messages;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Optional;

import lombok.Value;
import lombok.experimental.Accessors;

import com.google.common.base.Strings;
import com.google.common.net.MediaType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.zoomulus.speakeasy.core.message.Message;
import com.zoomulus.speakeasy.core.types.EmailAddress;
import com.zoomulus.speakeasy.sdp.messages.exceptions.SDPMessageException;
import com.zoomulus.speakeasy.sdp.messages.exceptions.SDPParseException;
import com.zoomulus.speakeasy.sdp.types.SDPBandwidth;
import com.zoomulus.speakeasy.sdp.types.SDPConnectionData;
import com.zoomulus.speakeasy.sdp.types.SDPOrigin;
import com.zoomulus.speakeasy.sdp.types.SDPTimePeriod;

/**
 * An implementation of RFC 4566.
 */

@Value
@Accessors(fluent=true)
public class SDPMessage implements Message
{
    public static final String VERSION = "0";
    
    final MediaType contentType = MediaType.create("application", "sdp");
    final String version;
    final SDPOrigin origin;
    final String sessionName;
    final Optional<String> sessionInformation;
    final Optional<URI> uri;
    final Optional<EmailAddress> emailAddress;
    final Optional<PhoneNumber> phoneNumber;
    final Optional<SDPConnectionData> connectionData;
    final Optional<SDPBandwidth> bandwidth;
    final SDPTimePeriod timing;

    @Override
    public ByteBuffer buffer()
    {
        // TODO Auto-generated method stub
        return null;
    }

    private SDPMessage(final String version,
            final SDPOrigin origin
            ) throws SDPMessageException
    {
        if (! VERSION.equals(version)) {
            throw new SDPMessageException(String.format("Invalid SDP version \"%s\"", version));
        }
        this.version = version;
        this.origin = origin;
        sessionName = new String(" ");
        sessionInformation = Optional.<String> empty();
        uri = Optional.<URI> empty();
        emailAddress = Optional.<EmailAddress> empty();
        phoneNumber = Optional.<PhoneNumber> empty();
        connectionData = Optional.<SDPConnectionData> empty();
        bandwidth = Optional.<SDPBandwidth> empty();
        timing = new SDPTimePeriod();
    }
    
    private static final String getNextExpectedToken()
    {
        return getNextExpectedToken(null);
    }
    
    private static final String getNextExpectedToken(final String currentToken)
    {
        if (Strings.isNullOrEmpty(currentToken)) { return Tokens.VERSION_TOKEN; }
        switch (currentToken) {
            case Tokens.VERSION_TOKEN:
                return Tokens.ORIGIN_TOKEN;
            default:
                return Tokens.NO_TOKEN;
        }
    }
    
    public static SDPMessage from(final ByteBuffer in) throws SDPMessageException, IOException
    {
        SDPMessageBuilder sdpBuilder = builder();
        String expectedToken = getNextExpectedToken();
        String value;
        
        while (in.hasRemaining())
        {
            char c = in.getChar();
            if (c != expectedToken.charAt(0))
            {
                throw new SDPParseException(String.format("SDP parse failure - expected %s but got %c", expectedToken, (char)c));
            }
            if ('=' != in.getChar())
            {
                throw new SDPParseException(String.format("SDP parse failure - token %c must be followed by '='", (char)c));
            }
            value = nextLine(in);
            
            switch (expectedToken)
            {
                case Tokens.VERSION_TOKEN:
                    sdpBuilder = sdpBuilder.version(value);
            }
            
            expectedToken = getNextExpectedToken(expectedToken);
        }
        return sdpBuilder.build();
    }
    
    static String nextLine(final ByteBuffer in)
    {
        final StringBuilder result = new StringBuilder();
        while (in.hasRemaining())
        {
            char c = in.getChar();
            if ('\n' == c) break;
            result.append(c);
        }
        return result.toString();
    }

    public static SDPMessageBuilder builder()
    {
        return new SDPMessageBuilder();
    }

    public static class SDPMessageBuilder
    {
        String version = VERSION;
        SDPOrigin origin = null;
        
        SDPMessageBuilder version(final String version)
        {
            this.version = version;
            return this;
        }
        
        public SDPMessageBuilder origin(final SDPOrigin origin) {
            this.origin = origin;
            return this;
        }
        
        public SDPMessage build() throws SDPMessageException
        {
            if (null == origin) origin = new SDPOrigin();
            return new SDPMessage(version, origin);
        }
    }
    
    public static class Tokens
    {
        static final String NO_TOKEN = "";
        
        public static final String VERSION_TOKEN = "v";
        public static final String ORIGIN_TOKEN = "o";
    }
}
