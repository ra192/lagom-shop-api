package io.dworkin.model;

/**
 * Created by yakov_000 on 10.03.2015.
 */
public class ProductEntity {

    private String code;
    private String displayName;
    private Double price;
    private String description;
    private String imageUrl;

    public ProductEntity(String code, String displayName, Double price, String description, String imageUrl) {
        this.code = code;
        this.displayName = displayName;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
