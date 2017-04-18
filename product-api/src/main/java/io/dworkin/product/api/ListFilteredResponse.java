package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.pcollections.PSequence;

/**
 * Created by yakov on 18.04.2017.
 */
@Immutable
@JsonSerialize
public final class ListFilteredResponse {
    public final PSequence<Product>products;
    public final Long count;

    @JsonCreator
    public ListFilteredResponse(@JsonProperty("products") PSequence<Product> products, @JsonProperty("count") Long count) {
        this.products = products;
        this.count = count;
    }
}
