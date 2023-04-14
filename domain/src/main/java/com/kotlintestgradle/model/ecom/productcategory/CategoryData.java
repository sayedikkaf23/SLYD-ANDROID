package com.kotlintestgradle.model.ecom.productcategory;

import com.kotlintestgradle.model.ecom.common.CategoryDataModel;
import java.util.ArrayList;

public class CategoryData {

  private ArrayList<CategoryDataModel> data;

  private String message;

  public CategoryData() {
  }

  public CategoryData(ArrayList<CategoryDataModel> data, String message) {
    this.data = data;
    this.message = message;
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

}
