package com.kotlintestgradle.interactor.ecom.address;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.CommonData;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.repository.EditAddressRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class EditAddressUseCase extends
    UseCase<EditAddressUseCase.RequestValues, EditAddressUseCase.ResponseValues> {

  private EditAddressRepository mRepository;

  @Inject
  public EditAddressUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      EditAddressRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.editAddress(requestValues.mItemData.getName(),
        requestValues.mItemData.getMobileNumber(),
        requestValues.mItemData.getMobileNumberCode(),
        requestValues.mItemData.getLocality(), requestValues.mItemData.getAddLine1(),
        requestValues.mItemData.getAddLine2(),
        requestValues.mItemData.getFlatNumber(), requestValues.mItemData.getLandmark(),
        requestValues.mItemData.getCity(), requestValues.mItemData.getState(),
        requestValues.mItemData.getCountry(), requestValues.mItemData.getPlaceId(),
        requestValues.mItemData.getPincode(), requestValues.mItemData.getLatitude(),
     requestValues.mItemData.getLongitude(), requestValues.mItemData.getTaggedAs(),
        requestValues.mItemData.getId(), requestValues.mItemData.getTagged(),
        requestValues.mItemData.isDefault(),requestValues.mItemData.getCityId(),requestValues.mItemData.getCountryId());
  }

  public static class RequestValues implements UseCase.RequestValues {
    private AddressListItemData mItemData;

    public RequestValues(AddressListItemData itemData) {
      if (itemData != null) {
        mItemData = itemData;
      } else {
        mItemData = new AddressListItemData();
      }
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {

    private CommonData mData;

    public ResponseValues(CommonData data) {
      this.mData = data;
    }

    public CommonData getData() {
      return mData;
    }

    public void setData(CommonData data) {
      this.mData = data;
    }
  }
}
