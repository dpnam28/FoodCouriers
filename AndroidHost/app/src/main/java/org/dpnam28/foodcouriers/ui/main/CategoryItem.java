package org.dpnam28.foodcouriers.ui.main;

public class CategoryItem {
    private final long id;
    private final String name;

    public CategoryItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
