package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Created by yakov on 24.03.2017.
 */
@Immutable
@JsonDeserialize
public class PropertyItem {

    public final String property;
    public final List<String> propertyValues;

    @JsonCreator
    public PropertyItem(String property, List<String> propertyValues) {
        this.property = property;
        this.propertyValues = propertyValues;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("property", property)
                .add("properties", propertyValues)
                .toString();
    }
}