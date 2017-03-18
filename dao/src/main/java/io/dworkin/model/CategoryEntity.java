package io.dworkin.model;

/**
 * Created by yakov_000 on 10.03.2015.
 */
public class CategoryEntity {

    private String name;
    private String displayName;

    public CategoryEntity(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
