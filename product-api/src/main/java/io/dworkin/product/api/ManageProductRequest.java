package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.pcollections.PSequence;

import javax.annotation.concurrent.Immutable;

/**
 * Created by yakov on 23.03.2017.
 */
@Immutable
@JsonDeserialize
public class ManageProductRequest {
    public final String code;
    public final String displayName;
    public final Double price;
    public final String description;
    public final String imageUrl;
    public final String category;
    public final PSequence<String> propertyValues;

    @JsonCreator
    public ManageProductRequest(String code, String displayName, Double price, String description, String imageUrl, String category, PSequence<String> propertyValues) {
        this.code = code;
        this.displayName = displayName;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.propertyValues = propertyValues;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("displayName", displayName)
                .add("price", price)
                .add("description", description)
                .add("imageUrl", imageUrl)
                .add("category", category)
                .add("propertyValues", propertyValues)
                .toString();
    }
}
