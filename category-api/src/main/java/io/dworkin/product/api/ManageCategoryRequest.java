package io.dworkin.product.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;
import java.util.Set;

/**
 * Create category request
 * Created by yakov on 14.03.2017.
 */
@Immutable
@JsonDeserialize
public final class ManageCategoryRequest {
    public final String name;
    public final String displayName;

    public final String parent;

    public final Set<String>properties;

    public final String token;

    @JsonCreator
    public ManageCategoryRequest(String name, String displayName, String parent, Set<String> properties, String token) {
        this.name = Preconditions.checkNotNull(name);
        this.displayName = Preconditions.checkNotNull(displayName);
        this.parent = parent;
        this.properties = properties;
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManageCategoryRequest that = (ManageCategoryRequest) o;
        return Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, displayName, parent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("displayName", displayName)
                .add("parent", parent)
                .add("properties",properties)
                .toString();
    }
}
