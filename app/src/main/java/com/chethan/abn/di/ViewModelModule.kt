package com.chethan.abn.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chethan.abn.view.venuedetail.VenueDetailViewModel
import com.chethan.abn.view.venuesearch.VenuesSearchListViewModel
import com.chethan.abn.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(VenuesSearchListViewModel::class)
    abstract fun bindUserViewModel(venuesViewModel: VenuesSearchListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(VenueDetailViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: VenueDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
