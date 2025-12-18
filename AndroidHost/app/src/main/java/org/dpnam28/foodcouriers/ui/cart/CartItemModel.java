package org.dpnam28.foodcouriers.ui.cart;

public class CartItemModel {
    private final long id;
    private final long foodId;
    private final String foodName;
    private final int quantity;
    private final double totalPrice;

    public CartItemModel(long id, long foodId, String foodName, int quantity, double totalPrice) {
        this.id = id;
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public long getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
