package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by yakov on 19.03.2017.
 */
@SuppressWarnings("serial")
@Immutable
@JsonSerialize
public final class Product {
    public final String code;
    public final String displayName;
    public final Double price;
    public final String description;
    public final String imageUrl;

    @JsonCreator
    public Product(String code, String displayName, Double price, String description, String imageUrl) {
        this.code = code;
        this.displayName = displayName;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
