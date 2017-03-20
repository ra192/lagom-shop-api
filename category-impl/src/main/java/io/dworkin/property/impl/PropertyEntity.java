package io.dworkin.property.impl;

/**
 * Created by yakov_000 on 16.03.2015.
 */
public class PropertyEntity {

    private String name;
    private String displayName;

    public PropertyEntity(String name, String displayName) {
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
