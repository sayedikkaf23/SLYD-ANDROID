package chat.hola.com.app.ecom.alldetails;

import static chat.hola.com.app.Utilities.Constants.TRUE;

import androidx.lifecycle.MutableLiveData;
import javax.inject.Inject;

/**
 * view mData class for the all details activity
 */
public class AllDetailsViewModel extends androidx.lifecycle.ViewModel {
  public androidx.databinding.ObservableField<String>
      mProductImage = new androidx.databinding.ObservableField<>();
  private MutableLiveData<Boolean> mLiveData = new MutableLiveData<>();

  /**
   * constructor class for the all details activity.
   */
  @Inject
  AllDetailsViewModel() {
  }

  /**
   * <p>this method is used listen when cross icon clicked.</p>
   */
  public void onCrossIconClicked() {
    mLiveData.postValue(TRUE);
  }

  /**
   * notify when cross icon clicked
   */
  MutableLiveData<Boolean> onCrossClicked() {
    return mLiveData;
  }
}