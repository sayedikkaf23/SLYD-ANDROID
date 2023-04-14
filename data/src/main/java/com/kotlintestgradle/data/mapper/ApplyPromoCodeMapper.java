package com.kotlintestgradle.data.mapper;

import static com.kotlintestgradle.data.utils.DataUtils.isEmptyArray;

import com.kotlintestgradle.model.ecom.getcart.QuantityData;
import com.kotlintestgradle.model.ecom.promocode.ApplyPromoCodeData;
import com.kotlintestgradle.model.ecom.promocode.ProductPromoInput;
import com.kotlintestgradle.model.ecom.promocode.TaxDetails;
import com.kotlintestgradle.remote.model.request.ecom.ApplyPromoProductRequest;
import com.kotlintestgradle.remote.model.request.ecom.TaxRequest;
import com.kotlintestgradle.remote.model.response.ecom.getcart.QuantityDetails;
import com.kotlintestgradle.remote.model.response.ecom.promocode.ApplyPromoCodeListData;
import java.util.ArrayList;

public class ApplyPromoCodeMapper {
  public ApplyPromoCodeData mapper(ApplyPromoCodeListData applyPromoCodeListData) {
    return new ApplyPromoCodeData(applyPromoCodeListData.getTotal_amt(),
        applyPromoCodeListData.getPrice(), applyPromoCodeListData.getDelivery_fee(),
        applyPromoCodeListData.getReduced_amt());
  }

  public ArrayList<ApplyPromoProductRequest> convertToPromoProductRequest(ArrayList<ProductPromoInput> productList) {
    ArrayList<ApplyPromoProductRequest> promoProductRequests = new ArrayList<>();
    if (!isEmptyArray(productList)) {
      for (ProductPromoInput input: productList) {
        ApplyPromoProductRequest request = new ApplyPromoProductRequest(
                input.getProduct_id(), input.getName(), input.getBrand_name(), input.getPrice(),
                input.getDelivery_fee(), input.getCategory_id(), input.getCentralproduct_id(),
                convertToInputTax(input.getTaxRequests()), convertToQuantityData(input.getQuantityData()), input.getStore_id(),
                input.getCityId(), input.getUnitPrice(), input.getTaxAmount());
        promoProductRequests.add(request);
      }
    }
    return promoProductRequests;
  }

  private ArrayList<TaxRequest> convertToInputTax(ArrayList<TaxDetails> tax) {
    ArrayList<TaxRequest> taxRequests = new ArrayList<>();
    if (!isEmptyArray(tax)) {
      for (TaxDetails details: tax) {
        TaxRequest request = new TaxRequest(details.getTaxId(),
                details.getTaxName(), details.getTaxValue(), details.getTotalValue());
        taxRequests.add(request);
      }
    }
    return taxRequests;
  }

  private QuantityDetails convertToQuantityData(QuantityData quantityData) {
    return new QuantityDetails(quantityData.getUnit(), quantityData.getValue());
  }
}
