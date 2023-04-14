package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.cache.entity.UserCart;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.interactor.ecom.CommonData;
import com.kotlintestgradle.model.ecom.shoppinglist.MyListItemData;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import com.kotlintestgradle.remote.model.response.ecom.shoppinglist.MyListDetails;
import com.kotlintestgradle.remote.model.response.ecom.shoppinglist.MyListItemDetails;
import java.util.ArrayList;
import java.util.List;

public class SuccessMapper {
  public CommonData mapper(CommonModel model) {
    return new CommonData(model.getMessage());
  }

  public ArrayList<MyListItemData> mapper(MyListDetails details, List<UserCart> cartList) {
    ArrayList<MyListItemData> itemData = new ArrayList<>();
    if (!DataUtils.isEmptyArray(details.getData())) {
      for (MyListItemDetails itemDetails : details.getData()) {

        MyListItemData data = new MyListItemData(itemDetails.getTitle(),
            itemDetails.getShoppingId(),
            itemDetails.getPenCount(), itemDetails.getBackgroundImage(),
            CommonMapper.convertToSecApiCategoryData(itemDetails.getProducts(), cartList));
        itemData.add(data);
      }
    }

    return itemData;
  }
}
