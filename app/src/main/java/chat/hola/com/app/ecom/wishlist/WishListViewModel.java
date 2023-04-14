package chat.hola.com.app.ecom.wishlist;

import static chat.hola.com.app.Utilities.Constants.DEFAULT_VAL;
import static chat.hola.com.app.Utilities.Constants.EMPTY;
import static chat.hola.com.app.Utilities.Constants.HIGH_TO_LOW_VAL;
import static chat.hola.com.app.Utilities.Constants.LOW_TO_HIGH_VAL;
import static chat.hola.com.app.Utilities.Constants.NEWEST_FIRST;
import static chat.hola.com.app.Utilities.Constants.NEWEST_FIRST_VAL;
import static chat.hola.com.app.ecom.wishlist.WishListUiAction.BACK;
import static com.kotlintestgradle.remote.RemoteConstants.HIGH_TO_LOW;
import static com.kotlintestgradle.remote.RemoteConstants.LOW_TO_HIGH;

import android.text.TextUtils;
import android.view.View;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.Utilities.BackButtonClickListener;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.wishlist.sort.SortViewModel;
import com.jio.consumer.domain.interactor.user.handler.UserHandler;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.ClearWishListUseCase;
import com.kotlintestgradle.interactor.ecom.DeleteWishListProductUseCase;
import com.kotlintestgradle.interactor.ecom.GetWishListUseCase;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import javax.inject.Inject;

/*
 * Purpose – This class holds all the logic of WishListActivity  and acting as an Mediator
 * between Activity and other layers
 * @author 3Embed
 * Created on Nov 25, 2019
 * Modified on
 */
public class WishListViewModel extends ViewModel implements SortViewModel,
    BackButtonClickListener {
  private UseCaseHandler mHandler;
  private GetWishListUseCase mGetWishListUseCase;
  private ClearWishListUseCase mClearWishListUseCase;
  public ObservableField<String> mError = new ObservableField<>();
  public ObservableField<Boolean> mIsEmpty = new ObservableField<>(false);
  private MutableLiveData<Boolean> mSuggestionNotFound = new MutableLiveData<>(false);
  public ObservableField<Boolean> mProgressStatus = new ObservableField<>(false);
  private DeleteWishListProductUseCase mDeleteWishListProductUseCase;
  private MutableLiveData<ArrayList<ProductsData>> mWishLIstLiveData = new MutableLiveData<>();
  private ArrayList<ProductsData> mProductsData = new ArrayList<>();
  private MutableLiveData<WishListUiAction> mUiActionMutableLiveData = new MutableLiveData<>();
  @Inject
  public UserHandler userInfoHandler;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Inject
  public WishListViewModel(UseCaseHandler handler,
      GetWishListUseCase getWishListUseCase, ClearWishListUseCase clearWishListUseCase,
      DeleteWishListProductUseCase deleteWishListProductUseCase) {
    this.mHandler = handler;
    this.mGetWishListUseCase = getWishListUseCase;
    this.mDeleteWishListProductUseCase = deleteWishListProductUseCase;
    this.mClearWishListUseCase = clearWishListUseCase;

  }

  /**
   * This method is to handle to Configuration changes
   */
  void getWishList() {
    getWishListFromApi(DEFAULT_VAL, EMPTY);
  }

  /**
   * This method is using as wishList action listener from PDP page
   */
  void subscribeToWishListChangeListener() {

    compositeDisposable.add(
        userInfoHandler.getWishListObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(productsData -> {
              if (productsData.isRemoved()) {
                mProductsData.remove(productsData);
                mWishLIstLiveData.setValue(mProductsData);
              } else {
                getWishList();
              }

            }));
  }

  /**
   * This Method is using to get all the wish list products
   */
  void deleteWishListProduct(String ipAddress, String productId, double lat, double lng) {

    mProgressStatus.set(true);
    DisposableSingleObserver<DeleteWishListProductUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<DeleteWishListProductUseCase.ResponseValues>() {

          @Override
          public void onSuccess(DeleteWishListProductUseCase.ResponseValues responseValues) {
            Utilities.printLog("WishLIstDelete Fail");
            mProgressStatus.set(false);
            mProductsData.remove(new ProductsData(productId));
            mWishLIstLiveData.setValue(mProductsData);
            if (Utilities.isEmptyArray(mProductsData)) {
              mIsEmpty.set(true);
            }
          }

          @Override
          public void onError(Throwable e) {
            mError.set(e.getMessage());
            mProgressStatus.set(false);
            Utilities.printLog("WishLIstDelete Fail" + e.getMessage());
          }
        };

    mHandler.execute(mDeleteWishListProductUseCase,
        new DeleteWishListProductUseCase.RequestValues(productId,
            ipAddress, lat, lng, "", ""),
        disposableSingleObserver);
  }

  /**
   * Getting all the wish list products from api
   *
   * @param sortType    this value is using to differentiate between different sort type
   * @param searchQuery search query we will send while searching for products
   */
  void getWishListFromApi(int sortType, String searchQuery) {
    mProgressStatus.set(true);
    DisposableSingleObserver<GetWishListUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetWishListUseCase.ResponseValues>() {

          @Override
          public void onSuccess(GetWishListUseCase.ResponseValues responseValues) {

            mProgressStatus.set(false);

            if (!TextUtils.isEmpty(searchQuery.trim())) {
              mSuggestionNotFound.setValue(Utilities.isEmptyArray(
                  responseValues.getData().getData()));
            }

            Utilities.printLog("WishLIst Success");
            if (responseValues.getData() != null && !Utilities.isEmptyArray(
                responseValues.getData().getData())) {
              mProductsData.clear();
              mProductsData.addAll(responseValues.getData().getData());
              mWishLIstLiveData.setValue(mProductsData);
            }
          }

          @Override
          public void onError(Throwable e) {
            if (TextUtils.isEmpty(searchQuery.trim())) {
              mIsEmpty.set(true);
            } else {
              mSuggestionNotFound.setValue(true);
            }
            mProgressStatus.set(false);
            Utilities.printLog("WishLIst Fail" + e.getMessage());
          }
        };

    if (TextUtils.isEmpty(searchQuery)) {
      mHandler.execute(mGetWishListUseCase, new GetWishListUseCase.RequestValues(sortType),
          disposableSingleObserver);
    } else {
      mHandler.execute(mGetWishListUseCase,
          new GetWishListUseCase.RequestValues(sortType, searchQuery),
          disposableSingleObserver);
    }
  }

  /**
   * Clear all the wish list products
   */
  void clearAllWishListProducts() {
    mProgressStatus.set(true);
    DisposableSingleObserver<ClearWishListUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<ClearWishListUseCase.ResponseValues>() {
          @Override
          public void onSuccess(ClearWishListUseCase.ResponseValues responseValues) {
            mProgressStatus.set(false);
            Utilities.printLog("WishLIst Success");
            mProductsData.clear();
            mWishLIstLiveData.setValue(mProductsData);
            mIsEmpty.set(true);

          }

          @Override
          public void onError(Throwable e) {
            mError.set(e.getMessage());
            mIsEmpty.set(true);
            mProgressStatus.set(false);
            Utilities.printLog("WishLIst Fail" + e.getMessage());
          }
        };

    mHandler.execute(mClearWishListUseCase, new ClearWishListUseCase.RequestValues(),
        disposableSingleObserver);
  }

  /**
   * This method is using to get mutable object for Wish list product changes
   *
   * @return mutable live mData to observe
   */
  MutableLiveData<ArrayList<ProductsData>> getWishLIstLiveData() {
    return mWishLIstLiveData;
  }

  @Override
  public void setDisposableSingleObserver() {
  }

  @Override
  public void getSubCatFilterProduct(String catName, String subCatName, String subSubCatName, int
   sortType, String page, String symptomName) {

  }

  @Override
  public void getSearchProduct(String searchQuery, int sortType) {
  }

  @Override
  public void wishLIstSort(int type) {
    switch (type) {
      case LOW_TO_HIGH:
        getWishListFromApi(LOW_TO_HIGH_VAL, EMPTY);
        break;
      case HIGH_TO_LOW:
        getWishListFromApi(HIGH_TO_LOW_VAL, EMPTY);
        break;
      case NEWEST_FIRST:
        getWishListFromApi(NEWEST_FIRST_VAL, EMPTY);
        break;
    }
  }

  /**
   * This method is using to get UI action Listener
   *
   * @return Ui action observable
   */
  MutableLiveData<WishListUiAction> getUiActionListener() {
    return mUiActionMutableLiveData;
  }

  /**
   * This method is using to get Empty screen mutable emitter
   *
   * @return boolean value emitter
   */
  MutableLiveData<Boolean> getSuggestionNotFound() {
    return mSuggestionNotFound;
  }

  @Override
  public void backButtonClickListener(View view) {
    mUiActionMutableLiveData.setValue(BACK);
  }

  /**
   * This method is using to dispose all the disposables
   */
  void destroyListener() {

    compositeDisposable.dispose();
  }
}