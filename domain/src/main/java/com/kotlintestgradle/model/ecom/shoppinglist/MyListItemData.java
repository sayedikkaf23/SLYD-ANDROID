package com.kotlintestgradle.model.ecom.shoppinglist;

import com.kotlintestgradle.model.ecom.home.CategoryData;
import java.util.ArrayList;

public class MyListItemData {

  private String title;

  private String shoppingId;

  private int penCount;

  private String backgroundImage;

  private ArrayList<CategoryData> products;

  public MyListItemData(String title, String shoppingId, int penCount,
      String backgroundImage,
      ArrayList<CategoryData> products) {
    this.title = title;
    this.shoppingId = shoppingId;
    this.penCount = penCount;
    this.backgroundImage = backgroundImage;
    this.products = products;
  }

  public MyListItemData(String title, String shoppingId) {
    this.title = title;
    this.shoppingId = shoppingId;
  }

  public MyListItemData(String shoppingId) {
    this.shoppingId = shoppingId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getShoppingId() {
    return shoppingId;
  }

  public void setShoppingId(String shoppingId) {
    this.shoppingId = shoppingId;
  }

  public int getPenCount() {
    return penCount;
  }

  public void setPenCount(int penCount) {
    this.penCount = penCount;
  }

  public String getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(String backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public ArrayList<CategoryData> getProducts() {
    return products;
  }

  public void setProducts(ArrayList<CategoryData> products) {
    this.products = products;
  }

  @Override
  public boolean equals(Object obj) {
    MyListItemData data = (MyListItemData) obj;
    return data.getShoppingId().equals(this.getShoppingId());
  }
}
