package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.help.HelpItemData;
import com.kotlintestgradle.model.ecom.help.HelpListData;
import com.kotlintestgradle.model.ecom.help.HelpSubCatListData;
import com.kotlintestgradle.remote.model.response.help.HelpItemDetails;
import com.kotlintestgradle.remote.model.response.help.HelpListDetails;
import com.kotlintestgradle.remote.model.response.help.HelpSubCatListDetails;
import java.util.ArrayList;

public class HelpMapper {
  public HelpListData mapper(HelpListDetails categoryDetails) {
    return new HelpListData(
        convertToLanguageItemData(categoryDetails.getData()), categoryDetails.getMessage()
    );
  }

  private ArrayList<HelpItemData> convertToLanguageItemData(
      ArrayList<HelpItemDetails> lanItemDetailsArrayList) {
    ArrayList<HelpItemData> lanDetailsArrayList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(lanItemDetailsArrayList)) {
      for (HelpItemDetails details : lanItemDetailsArrayList) {
        HelpItemData data = new HelpItemData(details.getDesc(),
            convertToSubCategoryData(details.getSubcat()), details.getLink(), details.getName());
        lanDetailsArrayList.add(data);
      }
    }
    return lanDetailsArrayList;
  }

  private ArrayList<HelpSubCatListData> convertToSubCategoryData(
      ArrayList<HelpSubCatListDetails> lanItemDetailsArrayList) {
    ArrayList<HelpSubCatListData> lanDetailsArrayList = new ArrayList<>();
    if (!DataUtils.isEmptyArray(lanItemDetailsArrayList)) {
      for (HelpSubCatListDetails details : lanItemDetailsArrayList) {
        HelpSubCatListData data = new HelpSubCatListData(details.getDesc(), details.getLink(),
            details.getName());
        lanDetailsArrayList.add(data);
      }
    }
    return lanDetailsArrayList;
  }
}
