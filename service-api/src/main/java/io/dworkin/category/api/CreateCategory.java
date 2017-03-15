package io.dworkin.category.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.Immutable;

/**
 * Create category request
 * Created by yakov on 14.03.2017.
 */
@Immutable
@JsonDeserialize
public final class CreateCategory {
    public final String name;
    public final String displayName;

    public final String parent;

    @JsonCreator
    public CreateCategory(String name, String displayName, String parent) {
        this.name = Preconditions.checkNotNull(name);
        this.displayName = Preconditions.checkNotNull(displayName);
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateCategory that = (CreateCategory) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(displayName, that.displayName) &&
                Objects.equal(parent, that.parent);
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
                .toString();
    }
}
