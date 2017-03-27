package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.pcollections.PSequence;

import javax.annotation.concurrent.Immutable;

/**
 * Created by yakov on 24.03.2017.
 */
@Immutable
@JsonDeserialize
public class Property {

    public final String name;
    public final PSequence<String> propertyValues;

    @JsonCreator
    public Property(@JsonProperty("name") String name, @JsonProperty("propertyValues") PSequence<String> propertyValues) {
        this.name = name;
        this.propertyValues = propertyValues;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("properties", propertyValues)
                .toString();
    }
}