package com.zoomulus.speakeasy.core.flow;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A Sink sends a response to some other source besides
 * a Node.  A Flow ends with a Sink.
 */
@Data
public abstract class Sink implements RespondingNode
{
    @Setter(AccessLevel.PACKAGE)
    @Accessors(fluent=true)
    private Flow flow;
}
