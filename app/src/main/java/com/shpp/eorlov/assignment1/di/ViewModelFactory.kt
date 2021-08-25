package com.shpp.eorlov.assignment1.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.dialogfragment.ContactDialogViewModel
import com.shpp.eorlov.assignment1.ui.mycontacts.MyContactsViewModel
import com.shpp.eorlov.assignment1.ui.myprofile.MyProfileViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

/*
    I've taken this code from here:
    https://proandroiddev.com/viewmodel-with-dagger2-architecture-components-2e06f06c9455
*/
@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MyContactsViewModel::class)
    internal abstract fun contactsViewModel(viewModel: MyContactsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactDialogViewModel::class)
    internal abstract fun contactDialogViewModel(viewModel: ContactDialogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    internal abstract fun sharedViewModel(viewModel: SharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyProfileViewModel::class)
    internal abstract fun myProfileViewModel(viewModel: MyProfileViewModel): ViewModel
}