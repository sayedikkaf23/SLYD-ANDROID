package chat.hola.com.app.ecom.help;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.TRUE;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.Utilities.Utilities;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.HelpUseCase;
import com.kotlintestgradle.model.ecom.help.HelpItemData;
import io.reactivex.observers.DisposableSingleObserver;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * view model class for help activity
 */
public class HelpViewModel extends ViewModel {
  public ObservableField<Boolean> progressVisible = new ObservableField(FALSE);
  private HelpUseCase mHelpUseCase;
  private UseCaseHandler mHandler;
  private MutableLiveData<ArrayList<HelpItemData>> mLiveData = new MutableLiveData<>();

  @Inject
  public HelpViewModel(HelpUseCase helpUseCase,
      UseCaseHandler handler) {
    this.mHelpUseCase = helpUseCase;
    this.mHandler = handler;
  }

  /**
   * call the get help  api
   */
  public void callGetHelpApi() {
    progressVisible.set(TRUE);
    DisposableSingleObserver<HelpUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<HelpUseCase.ResponseValues>() {
          @Override
          public void onSuccess(HelpUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            if (responseValues.getData() != null) {
              mLiveData.postValue(responseValues.getData().getData());
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            Utilities.printLog("AllBrands Fail" + e.getMessage());
          }
        };
    mHandler.execute(mHelpUseCase,
        new HelpUseCase.RequestValues(),
        disposableSingleObserver);
  }

  /**
   * notify activity when change help data comes
   */
  public MutableLiveData<ArrayList<HelpItemData>> getHelpData() {
    return mLiveData;
  }
}
