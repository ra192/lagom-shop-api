package io.dworkin.property.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Created by yakov_000 on 16.03.2015.
 */
@SuppressWarnings("serial")
@Immutable
@JsonSerialize
public final class Property {

    final private String name;
    final private String displayName;

    public Property(@JsonProperty("name") String name, @JsonProperty("displayName") String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }


    public String getDisplayName() {
        return displayName;
    }
}
