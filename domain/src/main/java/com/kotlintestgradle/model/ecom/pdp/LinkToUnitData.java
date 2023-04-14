package com.kotlintestgradle.model.ecom.pdp;

import java.util.ArrayList;

public class LinkToUnitData {
  private String image;

  private String childProductId;

  private boolean isPrimary;

  private String name;

  private String unitId;

  private ArrayList<SizeData> sizeData;

  public LinkToUnitData(String image, String childProductId, boolean isPrimary, String name,
      String unitId, ArrayList<SizeData> sizeData) {
    this.image = image;
    this.childProductId = childProductId;
    this.isPrimary = isPrimary;
    this.name = name;
    this.unitId = unitId;
    this.sizeData = sizeData;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getChildProductId() {
    return childProductId;
  }

  public void setChildProductId(String childProductId) {
    this.childProductId = childProductId;
  }

  public boolean getIsPrimary() {
    return isPrimary;
  }

  public void setIsPrimary(boolean isPrimary) {
    this.isPrimary = isPrimary;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  public ArrayList<SizeData> getSizeData() {
    return sizeData;
  }

  public void setSizeData(ArrayList<SizeData> sizeData) {
    this.sizeData = sizeData;
  }
}
