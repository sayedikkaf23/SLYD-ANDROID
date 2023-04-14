package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.model.ecom.wishlist.WishListData;
import com.kotlintestgradle.remote.model.response.ecom.wishlist.WishListDetails;

public class WishListMapper {

  public WishListData mapper(WishListDetails details, int storeType) {

    return new WishListData(CommonMapper.convertToProductData(details.getData(), storeType),
        details.getPenCount(),
        details.getMessage());
  }
}
