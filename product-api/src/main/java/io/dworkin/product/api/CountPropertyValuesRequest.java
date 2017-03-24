package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Created by yakov on 22.03.2017.
 */
@Immutable
@JsonDeserialize
public final class CountPropertyValuesRequest {
    public final String category;
    public final List<PropertyItem> properties;

    @JsonCreator
    public CountPropertyValuesRequest(@JsonProperty("category") String category, @JsonProperty("properties") List<PropertyItem> properties) {
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
