package org.dpnam28.foodcouriers.ui.search;

public class SearchResultItem {
    private final long id;
    private final String name;
    private final String description;
    private final Double price;
    private final String imageUrl;
    private final SearchContract.SearchType type;

    public SearchResultItem(long id, String name, String description, Double price, String imageUrl, SearchContract.SearchType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.type = type;
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

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public SearchContract.SearchType getType() {
        return type;
    }
}
