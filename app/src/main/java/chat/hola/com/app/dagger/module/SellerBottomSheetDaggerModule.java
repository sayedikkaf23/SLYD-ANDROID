package chat.hola.com.app.dagger.module;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.ecom.pdp.sellerbottomsheet.SellerBottomSheetFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SellerBottomSheetDaggerModule {
  @FragmentScoped
  @ContributesAndroidInjector
  abstract SellerBottomSheetFragment bindBottomSheet();
}
