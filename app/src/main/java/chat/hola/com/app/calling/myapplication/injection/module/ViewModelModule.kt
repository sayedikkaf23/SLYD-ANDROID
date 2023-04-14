package com.appscrip.myapplication.injection.module

import androidx.lifecycle.ViewModel
import com.appscrip.myapplication.injection.ViewModelKey
import com.appscrip.video.call.CallingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author 3Embed
 *
 * The ViewModelModule is used to provide a map of view models through dagger that is used by the ViewModelFactory class.
 *
 * @since 1.0(23-Aug-2019)
 *
 */
@Module
abstract class ViewModelModule : ViewModelFactory() {
    /*
      * This method basically says
      * inject this object into a Map using the @IntoMap annotation,
      * with the  DashboardViewModel.class as key,
      * and a Provider that will build a DashboardViewModel
      * object.
      *
      * */
    @Binds
    @IntoMap
    @ViewModelKey(CallingViewModel::class)
    protected abstract fun callingViewModel(callingViewModel: CallingViewModel): ViewModel
}