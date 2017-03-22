package io.dworkin.property.impl;

/**
 * Created by yakov_000 on 10.03.2015.
 */
public class PropertyValueEntity {

    private String name;
    private String displayName;

    public PropertyValueEntity(Long id, String name, String displayName, Long propertyId) {
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
