package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.List;

/**
 * Created by yakov on 22.03.2017.
 */
@Immutable
@JsonSerialize
public final  class CountPropertyValueResponse {
    public final List<CountPropertyItem> properties;
    public final List<List<CountPropertyItem>> additionalProperties;

    public CountPropertyValueResponse(List<CountPropertyItem> properties, List<List<CountPropertyItem>> additionalProperties) {
        this.properties = properties;
        this.additionalProperties = additionalProperties;
    }





    @Immutable
    @JsonSerialize
    public static final class CountPropertyItem {
        public final String name;
        public final String displayName;
        public final List<CountPropertyValueItem>propertyValues;

        @JsonCreator
        public CountPropertyItem(String name, String displayName, List<CountPropertyValueItem> propertyValues) {
            this.name = name;
            this.displayName = displayName;
            this.propertyValues = propertyValues;
        }
    }

    @Immutable
    @JsonSerialize
    public static final class CountPropertyValueItem {
        public final String name;
        public final String displayName;
        public final Long count;

        @JsonCreator
        public CountPropertyValueItem(String name, String displayName, Long count) {
            this.name = name;
            this.displayName = displayName;
            this.count = count;
        }
    }
}
