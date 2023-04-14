package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.promocode.ApplyPromoCodeData;
import com.kotlintestgradle.model.ecom.promocode.ProductPromoInput;
import com.kotlintestgradle.repository.ApplyPromoCodeRepository;
import io.reactivex.Single;
import java.util.ArrayList;
import javax.inject.Inject;

public class ApplyPromoCodesUseCase extends
    UseCase<ApplyPromoCodesUseCase.RequestValues, ApplyPromoCodesUseCase.ResponseValues> {
  private ApplyPromoCodeRepository mRepository;

  @Inject
  public ApplyPromoCodesUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      ApplyPromoCodeRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.applyPromoCode(requestValues.cityId, requestValues.countryId, requestValues.cityName, requestValues.countryName,
            requestValues.paymentMethodType, requestValues.mPromoCode,requestValues.mPromoId,
        requestValues.currencySymbol, requestValues.totalPurchaseValue, requestValues.cartId,
        requestValues.deliveryFee, requestValues.currencyName,requestValues.isWalletSelected,requestValues.products,
        requestValues.email);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private int paymentMethodType;
    private String mPromoCode;
    private String mPromoId;
    private String currencySymbol;
    private float totalPurchaseValue;
    private String cartId;
    private String cityId;
    private String countryId;
    private String cityName;
    private String countryName;
    private float deliveryFee;
    private boolean isWalletSelected;
    private String currencyName;
    private ArrayList<ProductPromoInput> products;
    private String email;
    public RequestValues(String cityId, String countryId, String cityName, String countryName,
                         int paymentMethodType, String promoCode,String promoId, String cuurencySymbol,
        float totalPurchaseValue, String cartId, float deliveryFee, boolean isWalletSelected, String currencyName,ArrayList<ProductPromoInput> products,
        String email) {
      this.paymentMethodType = paymentMethodType;
      this.mPromoCode = promoCode;
      this.mPromoId=promoId;
      this.currencySymbol = cuurencySymbol;
      this.totalPurchaseValue = totalPurchaseValue;
      this.cartId = cartId;
      this.cityId = cityId;
      this.countryId = countryId;
      this.cityName = cityName;
      this.countryName = countryName;
      this.deliveryFee = deliveryFee;
      this.currencyName = currencyName;
      this.isWalletSelected = isWalletSelected;
      this.products=products;
      this.email=email;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private ApplyPromoCodeData mData;

    public ResponseValues(ApplyPromoCodeData data) {
      this.mData = data;
    }

    public ApplyPromoCodeData getData() {
      return mData;
    }

    public void setData(ApplyPromoCodeData data) {
      this.mData = data;
    }
  }
}
