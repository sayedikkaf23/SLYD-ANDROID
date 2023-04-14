package com.appscrip.myapplication.injection

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * @author 3Embed
 *
 *ViewModelKey helps you map your ViewModel classes so that
 *BaseAppViewModelFactory can correctly provide/inject them.
 *
 * @since 1.0(23-Aug-2019)
 */

@Target(AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)