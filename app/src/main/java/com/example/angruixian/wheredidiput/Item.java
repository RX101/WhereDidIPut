package com.example.angruixian.wheredidiput;

import java.io.Serializable;

/**
 * Created by angruixian on 15/2/18.
 */

public class Item implements Serializable {
    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    private String itemTitle;
    private String itemPlace;
    private String itemImage;

    public Item(){

    }

    public Item(String itemTitle, String itemPlace) {
        this.itemTitle = itemTitle;
        this.itemPlace = itemPlace;
    }

    public Item(String itemTitle, String itemPlace, String itemImage) {
        this.itemTitle = itemTitle;
        this.itemPlace = itemPlace;
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemPlace() {
        return itemPlace;
    }

    public void setItemPlace(String itemPlace) {
        this.itemPlace = itemPlace;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    @Override
    public String toString() {
        return "Item{" +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemPlace='" + itemPlace + '\'' +
                ", itemImage='" + itemImage + '\'' +
                '}';
    }
}
