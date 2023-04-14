package com.kotlintestgradle.model.ecom.sellerlist;

public class ProductSeoData {

  private String description;

  private String metatags;

  private String title;

  private String slug;

  public ProductSeoData(String description, String metatags, String title, String slug) {
    this.description = description;
    this.metatags = metatags;
    this.title = title;
    this.slug = slug;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMetatags() {
    return metatags;
  }

  public void setMetatags(String metatags) {
    this.metatags = metatags;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }
}
