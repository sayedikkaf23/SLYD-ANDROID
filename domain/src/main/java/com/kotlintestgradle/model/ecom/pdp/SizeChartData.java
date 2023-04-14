package com.kotlintestgradle.model.ecom.pdp;

import java.util.ArrayList;

public class SizeChartData {
  private ArrayList<String> size;
  private String name;

  public SizeChartData(ArrayList<String> size, String name) {
    this.size = size;
    this.name = name;
  }

  public ArrayList<String> getSize() {
    return size;
  }

  public void setSize(ArrayList<String> size) {
    this.size = size;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
