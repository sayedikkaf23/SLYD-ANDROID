package chat.hola.com.app.dagger.module;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.ecom.pdp.attributebottomsheet.AttributesBottomSheetFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AttributesBottomSheetDaggerModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract AttributesBottomSheetFragment bindBottomSheet();


}