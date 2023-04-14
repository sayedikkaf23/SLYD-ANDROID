package com.appscrip.myapplication.injection.module

import androidx.lifecycle.ViewModelProvider
import com.appscrip.myapplication.injection.BaseAppViewModelFactory
import dagger.Binds
import dagger.Module

/**
 **@author 3Embed
 *
 * ViewModelFactory class basically helps you dynamically create ViewModels for your Activities and Fragments.
 * The ViewModelFactoryclass has a list of providers and can create any ViewModel that was bound.
 * Fragments and Activities can nowjust inject the factory and retrieve their ViewModel.
 *
 * @since 1.0 (23-Aug-2019)
 */
@Module
abstract class ViewModelFactory {
    @Binds
    abstract fun bindViewModelFactory(factory: BaseAppViewModelFactory): ViewModelProvider.Factory
}