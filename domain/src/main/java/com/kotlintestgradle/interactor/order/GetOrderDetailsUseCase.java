package com.kotlintestgradle.interactor.order;


import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.orderdetails.MasterOrderData;
import com.kotlintestgradle.model.orderdetails.pharmacy.MasterOrdersDetails;
import com.kotlintestgradle.model.orderdetails.pharmacy.StoreOrdersItemData;
import com.kotlintestgradle.repository.GetOrderDetailsRepository;

import javax.inject.Inject;

import io.reactivex.Single;

import static com.kotlintestgradle.DomainConstants.MASTER_ORDER_TYPE;
import static com.kotlintestgradle.DomainConstants.ORDER_STATUS_PHARMACY;

public class GetOrderDetailsUseCase extends
    UseCase<GetOrderDetailsUseCase.RequestValues, GetOrderDetailsUseCase.ResponseValues> {
  private GetOrderDetailsRepository mRepository;

  @Inject
  public GetOrderDetailsUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                GetOrderDetailsRepository getOrderDetailsRepository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = getOrderDetailsRepository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    if (requestValues.mStatus == ORDER_STATUS_PHARMACY) {
      if (requestValues.mType.equals(MASTER_ORDER_TYPE)) {
        return mRepository.getMasterOrderDetail(requestValues.mType, requestValues.mOrderId);
      } else {
        return mRepository.getStoreOrderDetail(requestValues.mType, requestValues.mOrderId);
      }
    } else {
      return mRepository.getOrderDetails(requestValues.mType, requestValues.mOrderId);
    }
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mType;
    private String mOrderId;
    private int mStatus;

    public RequestValues(String type, String orderId, int status) {
      mType = type;
      mOrderId = orderId;
      mStatus = status;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private MasterOrderData mData;
    private StoreOrdersItemData mStoreOrderData;
    private MasterOrdersDetails mMasterOrdersDetails;

    public ResponseValues(StoreOrdersItemData storeOrderData) {
      this.mStoreOrderData = storeOrderData;
    }

    public ResponseValues(MasterOrdersDetails data) {
      this.mMasterOrdersDetails = data;
    }

    public ResponseValues(MasterOrderData data) {
      mData = data;
    }

    public MasterOrdersDetails getMasterOrdersDetails() {
      return mMasterOrdersDetails;
    }

    public void setMasterOrdersDetails(MasterOrdersDetails masterOrdersDetails) {
      this.mMasterOrdersDetails = masterOrdersDetails;
    }

    public MasterOrderData getData() {
      return mData;
    }

    public void setData(MasterOrderData data) {
      mData = data;
    }

    public StoreOrdersItemData getStoreOrderData() {
      return mStoreOrderData;
    }

    public void setStoreOrderData(StoreOrdersItemData mStoreOrderData) {
      this.mStoreOrderData = mStoreOrderData;
    }
  }
}
