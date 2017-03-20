package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.concurrent.Immutable;

/**
 * Created by yakov on 14.03.2017.
 */
@SuppressWarnings("serial")
@Immutable
@JsonSerialize
public final class Category {

    public final String name;
    public final String displayName;

    @JsonCreator
    public Category(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
