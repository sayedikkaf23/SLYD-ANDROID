package com.kotlintestgradle.data.mapper;

import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.model.ecom.sellerlist.ExchangePolicyData;
import com.kotlintestgradle.model.ecom.sellerlist.ProductSeoData;
import com.kotlintestgradle.model.ecom.sellerlist.ReplacementPolicyData;
import com.kotlintestgradle.model.ecom.sellerlist.ReturnPolicyData;
import com.kotlintestgradle.model.ecom.sellerlist.SellerListData;
import com.kotlintestgradle.model.ecom.sellerlist.SellerListDataItem;
import com.kotlintestgradle.model.ecom.sellerlist.TermAndConditionData;
import com.kotlintestgradle.model.ecom.sellerlist.WarrantyData;
import com.kotlintestgradle.remote.model.response.ecom.sellerlist.ProductSeoDetails;
import com.kotlintestgradle.remote.model.response.ecom.sellerlist.SellerDetails;
import com.kotlintestgradle.remote.model.response.ecom.sellerlist.SellerDetailsItem;
import java.util.ArrayList;

public class SellerListMapper {
  public SellerListData mapper(SellerDetails sellerDetails) {
    return new SellerListData(convertToSelleListData(sellerDetails.getData()),
        sellerDetails.getMessage());
  }

  private ArrayList<SellerListDataItem> convertToSelleListData(
      ArrayList<SellerDetailsItem> detailsItemArrayList) {
    ArrayList<SellerListDataItem> listDataItems = new ArrayList<>();
    if (!DataUtils.isEmptyArray(detailsItemArrayList)) {

      for (SellerDetailsItem detailsItem : detailsItemArrayList) {

        ExchangePolicyData exchangePolicyData = detailsItem.getExchangePolicy() != null ? new
            ExchangePolicyData(detailsItem.getExchangePolicy().getNoofdays(),
            detailsItem.getExchangePolicy().isExchange()) : null;
        SellerListDataItem item = new SellerListDataItem(
            CommonMapper.convertToImageData(detailsItem.getImages()),
            detailsItem.getProductSeo() != null ? convertToProductSeoData(
                detailsItem.getProductSeo()) : null, exchangePolicyData,
            CommonMapper.convertToSupplierDataList(detailsItem.getSupplier()),
            detailsItem.getReturnPolicy() != null ? new ReturnPolicyData(
                detailsItem.getReturnPolicy().isReturn(),
                detailsItem.getReturnPolicy().getNoofdays()) : null,
            detailsItem.getCurrencySymbol(),
            detailsItem.getWarranty() != null ? new WarrantyData(detailsItem.getWarranty().getEn(),
                detailsItem.getWarranty().getAr())
                : null, detailsItem.getCurrency(), detailsItem.getTotalStarRating(),
            detailsItem.getReplacementPolicy() != null ? new ReplacementPolicyData(
                detailsItem.getReplacementPolicy().getNoofdays(),
                detailsItem.getReplacementPolicy().isReplacement()) : null,
            detailsItem.getTermAndcondition() != null ? new TermAndConditionData(
                detailsItem.getTermAndcondition().getEn(),
                detailsItem.getTermAndcondition().getEs()) : null, detailsItem.getProductName()
        );
        listDataItems.add(item);
      }
    }
    return listDataItems;
  }

  private ProductSeoData convertToProductSeoData(ProductSeoDetails details) {
    return new ProductSeoData(details.getDescription(), details.getMetatags(), details.getTitle(),
        details.getSlug());
  }

}
