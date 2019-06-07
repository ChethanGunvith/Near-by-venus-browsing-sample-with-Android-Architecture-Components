package com.chethan.abn.di

import com.chethan.abn.view.venuedetail.VenueDetailFragment
import com.chethan.abn.view.venuesearch.VenuesSearchListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun userSearchListFragment(): VenuesSearchListFragment

    @ContributesAndroidInjector
    abstract fun userSearchDetailFragment(): VenueDetailFragment
}
