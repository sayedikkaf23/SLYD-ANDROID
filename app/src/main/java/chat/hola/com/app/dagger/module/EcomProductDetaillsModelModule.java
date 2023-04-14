package chat.hola.com.app.dagger.module;

import androidx.lifecycle.ViewModel;
import chat.hola.com.app.ecom.pdp.ProductDetailsViewModel;
import chat.hola.com.app.ecom.pdp.attributebottomsheet.AttributesBottomSheetViewModel;
import chat.hola.com.app.ecom.pdp.sellerbottomsheet.SellerBottomSheetViewModel;
import com.appscrip.myapplication.injection.ViewModelKey;
import com.appscrip.myapplication.injection.module.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class EcomProductDetaillsModelModule extends ViewModelFactory {
  @Binds
  @IntoMap
  @ViewModelKey(ProductDetailsViewModel.class)
  protected abstract ViewModel homeViewModel(ProductDetailsViewModel productDetailsViewModel);

  @Binds
  @IntoMap
  @ViewModelKey(AttributesBottomSheetViewModel.class)
  protected abstract ViewModel attributeBottomSheet(
      AttributesBottomSheetViewModel attributesBottomSheetViewModel);

  @Binds
  @IntoMap
  @ViewModelKey(SellerBottomSheetViewModel.class)
  protected abstract ViewModel sellerBottomSheet(
      SellerBottomSheetViewModel attributesBottomSheetViewModel);
}