package com.zoomulus.speakeasy.sdp.messages;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Optional;

import lombok.Value;
import lombok.experimental.Accessors;

import com.zoomulus.speakeasy.core.message.Message;
import com.zoomulus.speakeasy.core.types.E164PhoneNumber;
import com.zoomulus.speakeasy.core.types.EmailAddress;

@Value
@Accessors(fluent=true)
public class SDPMessage implements Message
{
    final String contentType="application/sdp";
    final String version="0";
    final SDPOrigin origin;
    final String sessionName;
    final Optional<String> sessionInformation;
    final Optional<URI> uri;
    final Optional<EmailAddress> emailAddress;
    final Optional<E164PhoneNumber> phoneNumber;
    
    @Override
    public ByteBuffer buffer()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    private SDPMessage()
    {
        origin = new SDPOrigin();
        sessionName = new String(" ");
        sessionInformation = Optional.<String> empty();
        uri = Optional.<URI> empty();
        emailAddress = Optional.<EmailAddress> empty();
        phoneNumber = Optional.<E164PhoneNumber> empty();
    }    

    public static SDPMessageBuilder builder()
    {
        return new SDPMessageBuilder();
    }
    
    public static class SDPMessageBuilder
    {
        public SDPMessage build()
        {
            return new SDPMessage();
        }
    }
}
