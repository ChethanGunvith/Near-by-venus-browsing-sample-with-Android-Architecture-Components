/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.github.ui.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chethan.abn.model.Venue
import com.chethan.abn.model.VenueDetailsWithPhotos
import com.chethan.abn.repository.Resource
import com.chethan.abn.repository.VenueDetailsRepository
import com.chethan.abn.util.TestUtil
import com.chethan.abn.util.mock
import com.chethan.abn.view.venuedetail.VenueDetailViewModel
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class VenueDetailViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = Mockito.mock(VenueDetailsRepository::class.java)
    private var venueDetailsViewModel = VenueDetailViewModel(repository)

    @Test
    fun testNull() {
        MatcherAssert.assertThat(venueDetailsViewModel.venueDetails, CoreMatchers.notNullValue())
        Mockito.verify(repository, Mockito.never()).loadDetailVenue(Mockito.anyString())
    }

    @Test
    fun dontFetchWithoutObservers() {
        venueDetailsViewModel.setVenueId("5a2285eddee7701b1d63d2d3")
        Mockito.verify(repository, Mockito.never()).loadDetailVenue(Mockito.anyString())
    }

    @Test
    fun fetchWhenObserved() {
        venueDetailsViewModel.setVenueId("5a2285eddee7701b1d63d2d3")
        venueDetailsViewModel.venueDetails.observeForever(mock())
        Mockito.verify(repository).loadDetailVenue("5a2285eddee7701b1d63d2d3")
    }

    @Test
    fun changeWhileObserved() {
        venueDetailsViewModel.venueDetails.observeForever(mock())

        venueDetailsViewModel.setVenueId("5a2285eddee7701b1d63d2d3")
        venueDetailsViewModel.setVenueId("4b83cb72f964a520d71031e3")

        Mockito.verify(repository).loadDetailVenue("5a2285eddee7701b1d63d2d3")
        Mockito.verify(repository).loadDetailVenue("4b83cb72f964a520d71031e3")
    }

}