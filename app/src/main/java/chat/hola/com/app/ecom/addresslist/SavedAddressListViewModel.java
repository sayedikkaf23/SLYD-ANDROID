package chat.hola.com.app.ecom.addresslist;

import static chat.hola.com.app.Utilities.Constants.NONE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.ecom.addresslist.SavedAddressUiAction.BACK;
import static chat.hola.com.app.ecom.addresslist.SavedAddressUiAction.DELIVERY_AVAILABLE;
import static chat.hola.com.app.ecom.addresslist.SavedAddressUiAction.DELIVERY_NOT_AVAILABLE;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;

import android.text.TextUtils;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.Utilities.Utilities;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.address.DeleteAddressUseCase;
import com.kotlintestgradle.interactor.ecom.address.GetAddressUseCase;
import com.kotlintestgradle.interactor.ecom.address.GetDeliveryFeeUseCase;
import com.kotlintestgradle.interactor.ecom.address.MakeAddressDefaultUseCase;
import com.kotlintestgradle.interactor.ecom.cart.AddProductToCartUseCase;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import io.reactivex.observers.DisposableSingleObserver;
import java.util.ArrayList;
import javax.inject.Inject;

/*
 * Purpose – This class Holds All the Business logic of SaveAddressListActivity.
 * @author 3Embed
 * Created on Dec 10, 2019
 * Modified on
 */
public class SavedAddressListViewModel extends ViewModel {
  public ObservableField progressVisible = new ObservableField<>(false);
  public ObservableField<Boolean> mIsEmpty = new ObservableField<>(false);
  public ObservableField<String> mError = new ObservableField<>();
  private GetAddressUseCase mGetAddressUseCase;
  private UseCaseHandler mHandler;
  private DeleteAddressUseCase mDeleteAddressUseCase;
  private AddProductToCartUseCase mAddProductToCartUseCase;
  private GetDeliveryFeeUseCase mGetDeliveryFeeUseCase;
  private MutableLiveData<ArrayList<AddressListItemData>> mArrayListMutableLiveData =
      new MutableLiveData<>();
  private MutableLiveData<Integer> mAddressListLiveData =
      new MutableLiveData<>();
  private MutableLiveData<SavedAddressUiAction> mUiActionLiveData = new MutableLiveData<>();
  private ArrayList<AddressListItemData> mAddressListItemData = new ArrayList<>();
  private MakeAddressDefaultUseCase mMakeAddressDefaultUseCase;
  private boolean mIsEmptyList;
  String storeCategoryId = EMPTY_STRING, estimatedProductPrice = EMPTY_STRING;
  int lastSelectedPosition = NONE;

  @Inject
  public SavedAddressListViewModel(GetAddressUseCase getAddressUseCase,
      MakeAddressDefaultUseCase makeAddressDefaultUseCase,
      AddProductToCartUseCase addProductToCartUseCase,
      GetDeliveryFeeUseCase getDeliveryFeeUseCase,
      DeleteAddressUseCase deleteAddressUseCase,
      UseCaseHandler handler) {
    this.mDeleteAddressUseCase = deleteAddressUseCase;
    this.mAddProductToCartUseCase = addProductToCartUseCase;
    this.mGetAddressUseCase = getAddressUseCase;
    this.mHandler = handler;
    this.mGetDeliveryFeeUseCase = getDeliveryFeeUseCase;
    this.mMakeAddressDefaultUseCase = makeAddressDefaultUseCase;
  }

  /**
   * this method is using to get Address list
   */
  void getAddressList() {
    progressVisible.set(true);
    DisposableSingleObserver<GetAddressUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetAddressUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetAddressUseCase.ResponseValues responseValues) {
            progressVisible.set(false);
            if (responseValues.getData().getData() != null
                && responseValues.getData().getData().size() > ZERO) {
              mIsEmpty.set(false);
              mIsEmptyList = false;
              Utilities.printLog("AddressList Success" + responseValues.getData().getData().size());
              mAddressListItemData.clear();
              mAddressListItemData.addAll(responseValues.getData().getData());
              mArrayListMutableLiveData.postValue(mAddressListItemData);
            } else {
              mIsEmpty.set(true);
              mIsEmptyList = true;
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(false);
            mIsEmpty.set(true);
            mIsEmptyList = true;
            mError.set(e.getMessage());
            Utilities.printLog("AddressList Fail" + e.getMessage());
          }
        };
    mHandler.execute(mGetAddressUseCase, new GetAddressUseCase.RequestValues(),
        disposableSingleObserver);
  }

  /**
   * This method is using to get whether addressList is empty or not
   *
   * @return isEmptyValue
   */
  boolean getIsEmptyValue() {
    return mIsEmptyList;
  }

  /**
   * This method is using to delete item from address list
   *
   * @param addressId id of address need to delete
   */
  void deleteAddress(String addressId) {
    progressVisible.set(true);
    DisposableSingleObserver<DeleteAddressUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<DeleteAddressUseCase.ResponseValues>() {
          @Override
          public void onSuccess(DeleteAddressUseCase.ResponseValues responseValues) {
            Utilities.printLog("AddressList Success");
            progressVisible.set(false);
            mAddressListItemData.remove(new AddressListItemData(addressId));
            if (Utilities.isEmptyArray(mAddressListItemData)) {
              mIsEmpty.set(true);
            }
            mArrayListMutableLiveData.postValue(mAddressListItemData);
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(false);
            mError.set(e.getMessage());
            Utilities.printLog("AddressList Fail" + e.getMessage());
          }
        };
    mHandler.execute(mDeleteAddressUseCase, new DeleteAddressUseCase.RequestValues(addressId),
        disposableSingleObserver);
  }

  /**
   * This method is using to make the address as default
   *
   * @param addressId address information make address as default
   */
  void makeAddressAsDefault(String addressId, String prevDefAddressId, int lastSelectedAddress) {
    progressVisible.set(true);
    DisposableSingleObserver<MakeAddressDefaultUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<MakeAddressDefaultUseCase.ResponseValues>() {
          @Override
          public void onSuccess(MakeAddressDefaultUseCase.ResponseValues responseValues) {
            Utilities.printLog("MakeAddressDefault Success");
            progressVisible.set(false);
            lastSelectedPosition = lastSelectedAddress;
            pushUpdatedData(addressId, prevDefAddressId);
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(false);
            Utilities.printLog("MakeAddressDefault Fail" + e.getMessage());
            mError.set(e.getMessage());
            lastSelectedPosition = lastSelectedAddress;
            pushUpdatedData(prevDefAddressId, prevDefAddressId);
          }
        };
    mHandler.execute(mMakeAddressDefaultUseCase,
        new MakeAddressDefaultUseCase.RequestValues(addressId),
        disposableSingleObserver);
  }

  /**
   * THis method is using to update the address list
   *
   * @param addressId id to update addressList
   */
  private void pushUpdatedData(String addressId, String prevDefAddressId) {
    if (!TextUtils.isEmpty(prevDefAddressId)) {
      AddressListItemData prevData = new AddressListItemData(prevDefAddressId);
      int prevIndex = mAddressListItemData.indexOf(prevData);
      mAddressListItemData.get(prevIndex).setDefault(false);
    }
    if (!TextUtils.isEmpty(addressId)) {
      AddressListItemData data = new AddressListItemData(addressId);
      int index = mAddressListItemData.indexOf(data);
      mAddressListItemData.get(index).setDefault(true);
      mAddressListLiveData.setValue(index);
    }
    mArrayListMutableLiveData.setValue(mAddressListItemData);
  }

  /**
   * This method is using to get AddressList Mutable mData
   *
   * @return AddressList Mutable mData
   */
  MutableLiveData<ArrayList<AddressListItemData>> getAddressListLiveData() {
    return mArrayListMutableLiveData;
  }

  /**
   * This method is using to get AddressList Mutable mData
   *
   * @return AddressList Mutable mData
   */
  MutableLiveData<Integer> getAddressListData() {
    return mAddressListLiveData;
  }

  /**
   * This method is using to Listen Ui action
   *
   * @return Ui action Mutable mData
   */
  MutableLiveData<SavedAddressUiAction> getUiActionListener() {
    return mUiActionLiveData;
  }

  /**
   * send request to calculate delivery fee
   *
   * @param cartId    in order to calculate delivery fee according to address
   * @param addressId in order to calculate delivery fee according to address
   */
  public void calculateDeliveryFee(String cartId, String addressId) {
    DisposableSingleObserver<GetDeliveryFeeUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetDeliveryFeeUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetDeliveryFeeUseCase.ResponseValues responseValues) {
            Utilities.printLog("AddressList Pass");
            mUiActionLiveData.postValue(DELIVERY_AVAILABLE);
          }

          @Override
          public void onError(Throwable e) {
            Utilities.printLog("AddressList Fail" + e.getMessage());
            mUiActionLiveData.postValue(DELIVERY_NOT_AVAILABLE);
          }
        };
    mHandler.execute(mGetDeliveryFeeUseCase, new GetDeliveryFeeUseCase.RequestValues(
        cartId, addressId), disposableSingleObserver);
  }

  public void clickBack(){
    mUiActionLiveData.postValue(BACK);
  }
}
