package io.dworkin.category.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.pcollections.PSequence;

import javax.annotation.concurrent.Immutable;

/**
 * Created by yakov on 22.03.2017.
 */
@Immutable
@JsonDeserialize
public final class CountPropertyValuesRequest {
    public final String category;
    public final PSequence<PropertyRequest> properties;

    @JsonCreator
    public CountPropertyValuesRequest(@JsonProperty("category") String category, @JsonProperty("properties") PSequence<PropertyRequest> properties) {
        this.category = Preconditions.checkNotNull(category);
        this.properties = properties;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("category", category)
                .add("properties", properties)
                .toString();
    }
}
