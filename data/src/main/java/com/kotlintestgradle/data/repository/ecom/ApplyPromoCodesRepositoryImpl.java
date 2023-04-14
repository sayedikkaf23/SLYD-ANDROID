package com.kotlintestgradle.data.repository.ecom;

import static com.kotlintestgradle.data.utils.DataConstants.ONE;
import static com.kotlintestgradle.data.utils.DataConstants.ZERO;
import static com.kotlintestgradle.remote.RemoteConstants.ECOM_PARTNER;
import static com.kotlintestgradle.remote.RemoteConstants.REGION_APPLIED;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.ApplyPromoCodeMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.data.utils.DataUtils;
import com.kotlintestgradle.interactor.ecom.ApplyPromoCodesUseCase;
import com.kotlintestgradle.model.ecom.promocode.ProductPromoInput;
import com.kotlintestgradle.remote.model.request.ecom.ApplyPromoCodeRequest;
import com.kotlintestgradle.remote.model.request.ecom.ApplyPromoProductRequest;
import com.kotlintestgradle.remote.model.response.ecom.getcart.QuantityDetails;
import com.kotlintestgradle.remote.model.response.ecom.promocode.ApplyPromoCodeListData;
import com.kotlintestgradle.repository.ApplyPromoCodeRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import java.util.ArrayList;
import javax.inject.Inject;

public class ApplyPromoCodesRepositoryImpl extends BaseRepository implements
    ApplyPromoCodeRepository {
  private DataSource mDataSource;
  private PreferenceManager preference;
  private ApplyPromoCodeMapper mMapper = new ApplyPromoCodeMapper();

  @Inject
  public ApplyPromoCodesRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
    preference = dataSource.preference();
  }

  @Override
  public Single<ApplyPromoCodesUseCase.ResponseValues> applyPromoCode(String cityId, String countryId, String cityName, String countryName,
      int paymentMethodType, String promoCode, String promoId, String currencySymbol,
      float totalPurchaseValue,
      String cartId,
      float deliveryFee, String currencyName, boolean isWalletSelected,
      ArrayList<ProductPromoInput> productPromoInputArrayList,String email) {
    return mDataSource.api().handler().applyPromoCode(new ApplyPromoCodeRequest(email,
                    paymentMethodType, ONE, currencySymbol, promoCode, promoId,
                    totalPurchaseValue, cartId, countryId, cityId,
                    deliveryFee, currencyName, preference.getUserId(),
                    ECOM_PARTNER, REGION_APPLIED, isWalletSelected ? ONE : ZERO, "",
                    countryName, cityName,
                    0, mMapper.convertToPromoProductRequest(productPromoInputArrayList))
    ).flatMap(new Function<ApplyPromoCodeListData, SingleSource<?
            extends ApplyPromoCodesUseCase.ResponseValues>>() {
          @Override
          public SingleSource<? extends ApplyPromoCodesUseCase.ResponseValues> apply(
              ApplyPromoCodeListData details) {
            return Single.just(
                new ApplyPromoCodesUseCase.ResponseValues(mMapper.mapper(details)));
          }
        });
  }

  private ArrayList<ApplyPromoProductRequest> getProductsList(
      ArrayList<ProductPromoInput> productPromoInputArrayList) {
    ArrayList<ApplyPromoProductRequest> applyPromoProductRequests = new ArrayList<>();
    if (!DataUtils.isEmptyArray(productPromoInputArrayList)) {
      for (ProductPromoInput details : productPromoInputArrayList) {
        ApplyPromoProductRequest promoProductRequest = new ApplyPromoProductRequest();
        promoProductRequest.setBrand_name(
            details.getBrand_name() != null && !details.getBrand_name().isEmpty()
                ? details.getBrand_name() : "");
        promoProductRequest.setProduct_id(
            details.getProduct_id() != null && !details.getProduct_id().isEmpty()
                ? details.getProduct_id() : "");
        promoProductRequest.setName(
            details.getName() != null && !details.getName().isEmpty()
                ? details.getName() : "");
        promoProductRequest.setPrice(details.getPrice());
        promoProductRequest.setDelivery_fee(details.getDelivery_fee());
        promoProductRequest.setCityId(details.getCityId());
        promoProductRequest.setUnitPrice(details.getUnitPrice());
        promoProductRequest.setTaxAmount(details.getTaxAmount());
        promoProductRequest.setCentralproduct_id(details.getCentralproduct_id());
        promoProductRequest.setTax(null);
        promoProductRequest.setQuantity(new QuantityDetails(details.getQuantityData().getUnit(),
            details.getQuantityData().getValue()));
        promoProductRequest.setCategory_id(
            details.getCategory_id() != null && !details.getCategory_id().isEmpty()
                ? details.getCategory_id() : null);
        promoProductRequest.setStore_id(
            details.getStore_id() != null && !details.getStore_id().isEmpty()
                ? details.getStore_id() : null);
        applyPromoProductRequests.add(promoProductRequest);
      }
    }
    return applyPromoProductRequests;
  }
}
