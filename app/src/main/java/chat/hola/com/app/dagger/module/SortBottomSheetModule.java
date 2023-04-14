package chat.hola.com.app.dagger.module;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.ecom.wishlist.sort.SortBottomSheet;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SortBottomSheetModule {

  @FragmentScoped
  @ContributesAndroidInjector
  abstract SortBottomSheet bindSortBottomSheet();
}
