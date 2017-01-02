package com.realbadapps.pendulum.server.media;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "room", value = SimpleIllumination.class),
        @JsonSubTypes.Type(name = "spots", value = MultiIllumination.class)})
public abstract class Illumination extends Framed {
    //
}