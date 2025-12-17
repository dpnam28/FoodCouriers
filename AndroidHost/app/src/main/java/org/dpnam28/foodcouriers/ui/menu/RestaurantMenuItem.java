package org.dpnam28.foodcouriers.ui.menu;

public class RestaurantMenuItem {
    private final long id;
    private final String name;
    private final String description;
    private final double price;
    private final String imageUrl;

    public RestaurantMenuItem(long id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
