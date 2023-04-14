package com.kotlintestgradle.model.ecom.common;

import java.util.ArrayList;

public class CategoryData {
  private ArrayList<CategoryDataModel> data;
  private String message;
  private int type;

  public CategoryData(ArrayList<CategoryDataModel> data, String message, int type) {
    this.data = data;
    this.message = message;
    this.type = type;
  }

  public ArrayList<CategoryDataModel> getData() {
    return data;
  }

  public void setData(ArrayList<CategoryDataModel> data) {
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
