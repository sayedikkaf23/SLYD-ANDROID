package chat.hola.com.app.ecom.addresslist.manageaddress;

import static chat.hola.com.app.Utilities.Constants.EDIT_TYPE;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_ADDRESS;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_AREA;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_CITY;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_COUNTRY;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_NAME;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_PHONE;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressFields.INVALID_POSTAL_CODE;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressUiAction.FINISH;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressUiAction.RIDE_ADDRESS_DONE;
import static chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressUiAction.RIDE_CURRENT_LOC_ICON;

import android.text.TextUtils;
import android.view.View;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.address.AddAddressUseCase;
import com.kotlintestgradle.interactor.ecom.address.EditAddressUseCase;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import io.reactivex.observers.DisposableSingleObserver;
import javax.inject.Inject;

/*
 * Purpose – This class Holds All the Business logic of AddAddressActivity.
 * @author 3Embed
 * Created on Dec 10, 2019
 * Modified on
 */
public class AddAddressViewModel extends ViewModel {
  public ObservableField<String> isDataFound = new ObservableField<>();
  public ObservableField<Boolean> isNameValid = new ObservableField<>(false);
  public ObservableField<Boolean> addressLine1Valid = new ObservableField<>(false);
  public ObservableField<Boolean> isPostalCodeValid = new ObservableField<>(false);
  public ObservableField<Boolean> isCountryValid = new ObservableField<>(false);
  public ObservableField<Boolean> isCityValid = new ObservableField<>(false);
  public ObservableField<Boolean> isAreaValid = new ObservableField<>(false);
  private boolean mIsNameValid, mAddressLine1Valid, mIsPostalCodeValid, mIsCountryValid, mIsCityValid, mIsAreaValid;
  public ObservableField<Boolean> canActivateBtn = new ObservableField<>(false);
  private AddAddressUseCase mAddAddressUseCase;
  private UseCaseHandler mHandler;
  private EditAddressUseCase mEditAddressUseCase;
  private String mName, mAddressLine1, mAddressLine2, mPhoneNum, mArea, mStateName;
  public boolean isApiCall = true;
  private String mCity, mCountry, mPostalCode, mLandMark, mCountryCode, mAddressTypeVal;
  private int mAddressType;
  private String mLatitude, mLongitude;
  private boolean mIsPhoneNumValid;
  private AddressListItemData mAddressListItemData;
  private int mUpdateType;
  private MutableLiveData<AddAddressUiAction> mAddAddressUiAction = new MutableLiveData<>();
  private MutableLiveData<AddAddressFields> mAddAddressFormAction = new MutableLiveData<>();

  @Inject
  public AddAddressViewModel(AddAddressUseCase addAddressUseCase,
      EditAddressUseCase editAddressUseCase,
      UseCaseHandler handler) {
    this.mAddAddressUseCase = addAddressUseCase;
    this.mEditAddressUseCase = editAddressUseCase;
    this.mHandler = handler;
  }

  /**
   * handles text changes of name
   */
  public void onTextChangeName(CharSequence s, int start, int before, int count) {
    mName = s.toString().trim();
    if (isEditable()) {
      mAddressListItemData.setName(mName);
    }
    isNameValid.set(TextUtils.isEmpty(mName));
    mIsNameValid = !TextUtils.isEmpty(mName);
    validateAddressPage();
  }

  /**
   * handles text changes of mobNum
   */
  public void onTextChangeMobNum(CharSequence s, int start, int before, int count) {
    mPhoneNum = s.toString().trim();
    if (isEditable()) {
      mAddressListItemData.setMobileNumber(mPhoneNum);
    }
    validateAddressPage();
  }

  /**
   * handles text changes of AddressLine1
   */
  public void onTextChangeAddressLineOne(CharSequence s, int start, int before, int count) {
    mAddressLine1 = s.toString().trim();
    if (isEditable()) {
      mAddressListItemData.setAddLine1(mAddressLine1);
    }
    addressLine1Valid.set(TextUtils.isEmpty(mAddressLine1));
    mAddressLine1Valid = !TextUtils.isEmpty(mAddressLine1);
    validateAddressPage();
  }

  /**
   * handles text changes of AddressLine2
   */
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    mAddressLine2 = s.toString().trim();
    if (isEditable()) {
      mAddressListItemData.setAddLine2(mAddressLine2);
    }
    validateAddressPage();
  }

  /**
   * handles text changes of City
   */
  public void onTextChangeCity(CharSequence s, int start, int before, int count) {
    mCity = s.toString().trim();
    if (isEditable()) {
      mAddressListItemData.setCity(mCity);
    }
    isCityValid.set(TextUtils.isEmpty(mCity));
    mIsCityValid = !TextUtils.isEmpty(mCity);
    validateAddressPage();
  }

  /**
   * handles text changes of Country
   */
  public void onTextChangeCountry(CharSequence s, int start, int before, int count) {
    mCountry = s.toString();
    if (isEditable()) {
      mAddressListItemData.setCountry(mCountry);
    }
    isCountryValid.set(TextUtils.isEmpty(mCountry));
    mIsCountryValid = !TextUtils.isEmpty(mCountry);
    validateAddressPage();
  }

  /**
   * handles text changes of Postal code
   */
  public void onTextChangePostalCode(CharSequence s, int start, int before, int count) {
    mPostalCode = s.toString();
    if (isEditable()) {
      mAddressListItemData.setPincode(mPostalCode);
    }
    isPostalCodeValid.set(TextUtils.isEmpty(mPostalCode));
    mIsPostalCodeValid = !TextUtils.isEmpty(mPostalCode);
    validateAddressPage();
  }

  /**
   * handles text changes of LandMark
   */
  public void onTextChangeLandMark(CharSequence s, int start, int before, int count) {
    mLandMark = s.toString();
    if (isEditable()) {
      mAddressListItemData.setLandmark(mLandMark);
    }
    validateAddressPage();
  }

  /**
   * handles text changes of Other Adress type
   */
  public void onTextChangeOtherAddress(CharSequence s, int start, int before, int count) {
    mAddressTypeVal = s.toString();
    if (isEditable()) {
      mAddressListItemData.setTaggedAs(mAddressTypeVal);
    }
    validateAddressPage();
  }

  /**
   * handles text changes of AreaName
   */
  public void onTextChangeAreaName(CharSequence s, int start, int before, int count) {
    mArea = s.toString();
    if (isEditable()) {
      mAddressListItemData.setLocality(mArea);
    }
    isAreaValid.set(TextUtils.isEmpty(mArea));
    mIsAreaValid = !TextUtils.isEmpty(mArea);
    validateAddressPage();
  }

  /**
   * this method is using to setLatLong value for selected values in Map
   */
  void setLatLong(String latitude, String longitude, String stateName) {
    this.mLatitude = latitude;
    this.mLongitude = longitude;
    this.mStateName = stateName;
    if (isEditable()) {
      mAddressListItemData.setLatitude(mLatitude);
      mAddressListItemData.setLongitude(mLongitude);
      mAddressListItemData.setState(mStateName);
    }
  }

  /**
   * this method is using to set address value selected from google map
   *
   * @param addressLine1 addressLine1
   * @param area         area name
   * @param city         cityName
   * @param country      country name
   * @param postalCode   pin code
   */
  void setInitialAddressFieldValues(String addressLine1, String area, String city,
      String country,
      String postalCode) {
    this.mCity = city;
    this.mPostalCode = postalCode;
    this.mCountry = country;
    this.mArea = area;
    this.mAddressLine1 = addressLine1;
  }

  /**
   * checking whether it's Edit flow or not
   *
   * @return is Edit flow
   */
  private boolean isEditable() {
    return mUpdateType == EDIT_TYPE && mAddressListItemData != null;
  }

  /**
   * Set CountryCode and phone Number status
   *
   * @param countryCode   selected country code
   * @param isPhoNumValid is Phone number valid or not
   */
  void setPhoneNum(String countryCode, boolean isPhoNumValid) {
    mIsPhoneNumValid = isPhoNumValid;
    mCountryCode = countryCode;
    validateAddressPage();
  }

  /**
   * This method is using for validation for maintaining Button status
   */
  private void validateAddressPage() {
    if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mAddressLine1) || TextUtils.isEmpty(
        mPostalCode) || TextUtils.isEmpty(mCity) || TextUtils.isEmpty(mArea) || TextUtils.isEmpty(
        mCountry) || !mIsPhoneNumValid) {
      canActivateBtn.set(false);
      return;
    }
    canActivateBtn.set(true);
  }

  /**
   * if it's Edit Address flow set initial values of the page
   *
   * @param addressListItemData Address mData
   * @param updateType          type of the page flow
   */
  void setAddressDataForEdit(AddressListItemData addressListItemData, int updateType) {
    this.mAddressListItemData = addressListItemData;
    this.mUpdateType = updateType;
    mName = mAddressListItemData.getName();
    mAddressLine1 = mAddressListItemData.getAddLine1();
    mCity = mAddressListItemData.getCity();
    mIsPhoneNumValid = true;
    mArea = mAddressListItemData.getLocality();
    mPostalCode = mAddressListItemData.getPincode();
    mAddressType = mAddressListItemData.getTagged();
    mAddressTypeVal = mAddressListItemData.getTaggedAs();
    validateAddressPage();
  }

  /**
   * Deciding USeCase for Save Button Click
   */
  void saveBtnClickAction() {
    if (isAddressValid()) {
      if (mUpdateType == EDIT_TYPE) {
        editAddress();
      } else {
        addAddress();
      }
    }
  }

  /**
   * check if entered address is valid or not
   * @return boolean
   */
  private boolean isAddressValid() {
    if (!mIsNameValid) {
      mAddAddressFormAction.postValue(INVALID_NAME);
      return false;
    }
    if (!mIsPhoneNumValid) {
      mAddAddressFormAction.postValue(INVALID_PHONE);
      return false;
    }
    if (!mAddressLine1Valid) {
      mAddAddressFormAction.postValue(INVALID_ADDRESS);
      return false;
    }
    if (!mIsAreaValid) {
      mAddAddressFormAction.postValue(INVALID_AREA);
      return false;
    }
    if (!mIsCityValid) {
      mAddAddressFormAction.postValue(INVALID_CITY);
      return false;
    }
    if (!mIsCountryValid) {
      mAddAddressFormAction.postValue(INVALID_COUNTRY);
      return false;
    }
    if (!mIsPostalCodeValid) {
      mAddAddressFormAction.postValue(INVALID_POSTAL_CODE);
      return false;
    }
    return true;
  }

  /**
   * This method is using to send address to Server using post API
   */
   void addAddress() {
    DisposableSingleObserver<AddAddressUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<AddAddressUseCase.ResponseValues>() {

          @Override
          public void onSuccess(AddAddressUseCase.ResponseValues responseValues) {
            mAddAddressUiAction.setValue(FINISH);
          }

          @Override
          public void onError(Throwable e) {
            isDataFound.set(e.getMessage());
          }
        };
    mHandler.execute(mAddAddressUseCase,
        new AddAddressUseCase.RequestValues(isApiCall,mName, mPhoneNum, mCountryCode, mArea, mAddressLine1,
            mAddressLine2, mLandMark, mCity, mCountry, "", mPostalCode, mLatitude, mLongitude,
            mAddressTypeVal, mStateName, mAddressType),
        disposableSingleObserver);

  }

  /**
   * This Method is Using to Set Address type
   *
   * @param addressVal address type value
   * @param type       address type
   */
  void setAddressType(String addressVal, int type) {
    mAddressType = type;
    mAddressTypeVal = addressVal;
    if (mAddressListItemData != null) {
      mAddressListItemData.setTagged(mAddressType);
      mAddressListItemData.setTaggedAs(mAddressTypeVal);
    }
  }

  /**
   * This method is using to edit selected address
   */
  private void editAddress() {
    DisposableSingleObserver<EditAddressUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<EditAddressUseCase.ResponseValues>() {

          @Override
          public void onSuccess(EditAddressUseCase.ResponseValues responseValues) {
            mAddAddressUiAction.setValue(FINISH);
          }

          @Override
          public void onError(Throwable e) {

          }
        };
    mHandler.execute(mEditAddressUseCase,
        new EditAddressUseCase.RequestValues(mAddressListItemData),
        disposableSingleObserver);

  }

  /**
   * This method is using to get the UI action LiveData object
   *
   * @return Ui action liveData
   */
  MutableLiveData<AddAddressUiAction> getUiActionLiveData() {
    return mAddAddressUiAction;
  }

  /**
   * This method is using to get the invalid form items LiveData object
   *
   * @return invalid form action liveData
   */
  MutableLiveData<AddAddressFields> getLiveFormData() {
    return mAddAddressFormAction;
  }

 /**
   * listens when where to view clicked
   */
  public void rideAddressDoneOnClick() {
    mAddAddressUiAction.postValue(RIDE_ADDRESS_DONE);
  }

  /**
   * listens when where to view clicked
   */
  public void rideCurrentLocation() {
    mAddAddressUiAction.postValue(RIDE_CURRENT_LOC_ICON);
  }

  public void clickBack(){
    mAddAddressUiAction.setValue(FINISH);
  }
}