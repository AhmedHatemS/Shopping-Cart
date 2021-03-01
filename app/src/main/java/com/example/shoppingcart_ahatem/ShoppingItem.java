package com.example.shoppingcart_ahatem;

import java.io.Serializable;

public class ShoppingItem implements Serializable {
    String itemName;
    int itemPrice;

    public ShoppingItem(String itemName, int itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
