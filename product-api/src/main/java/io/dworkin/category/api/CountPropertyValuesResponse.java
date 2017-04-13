package io.dworkin.category.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.pcollections.PSequence;

/**
 * Created by yakov on 22.03.2017.
 */
@Immutable
@JsonSerialize
public final class CountPropertyValuesResponse {
    public final PSequence<PropertyWithCount> properties;
    public final PSequence<PropertyWithCount> additionalProperties;
    public final PSequence<PropertyWithCount> selectedProperties;

    @JsonCreator
    public CountPropertyValuesResponse(@JsonProperty("properties") PSequence<PropertyWithCount> properties,
                                       @JsonProperty("additionalProperties") PSequence<PropertyWithCount> additionalProperties,
                                       @JsonProperty("selectedProperties") PSequence<PropertyWithCount> selectedProperties) {
        this.properties = properties;
        this.additionalProperties = additionalProperties;
        this.selectedProperties = selectedProperties;
    }


}
