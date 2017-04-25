package io.dworkin.product.api;

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
    public final String searchText;

    @JsonCreator
    public CountPropertyValuesRequest(@JsonProperty("category") String category,
                                      @JsonProperty("properties") PSequence<PropertyRequest> properties,
                                      @JsonProperty("searchText") String searchText) {
        this.category = Preconditions.checkNotNull(category);
        this.properties = properties;
        this.searchText = searchText;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("category", category)
                .add("properties", properties)
                .add("searchText", searchText)
                .toString();
    }
}
