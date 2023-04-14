package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.ApplyPromoCodesUseCase;
import com.kotlintestgradle.model.ecom.promocode.ProductPromoInput;
import io.reactivex.Single;
import java.util.ArrayList;

public interface ApplyPromoCodeRepository {

  Single<ApplyPromoCodesUseCase.ResponseValues> applyPromoCode(String cityId, String countryId,
      String cityName, String countryName,
      int paymentMethodType,
      String promoCode, String promoId,
      String currencySymbol,
      float totalPurchaseValue, String cartId, float deliveryFee, String currencyName,
      boolean isWalletSelected,
      ArrayList<ProductPromoInput> productPromoInputArrayList,String email);
}