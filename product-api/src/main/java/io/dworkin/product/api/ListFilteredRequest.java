package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Created by yakov on 19.03.2017.
 */
@Immutable
@JsonDeserialize
public final class ListFilteredRequest {
    public final String category;
    public final List<PropertyItem> properties;
    public final Integer first;
    public final Integer max;
    public final String orderBy;
    public final Boolean isAsc;

    @JsonCreator
    public ListFilteredRequest(@JsonProperty("category") String category, @JsonProperty("properties") List<PropertyItem> properties,
                               @JsonProperty("first") Integer first, @JsonProperty("max") Integer max,
                               @JsonProperty("orderBy") String orderBy, @JsonProperty("isAsk") Boolean isAsc) {
        this.category = Preconditions.checkNotNull(category);
        this.properties = properties;
        this.first = first;
        this.max = max;
        this.orderBy = orderBy;
        this.isAsc = isAsc;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("category", category)
                .add("properties", properties)
                .toString();
    }
}
