package com.chethan.abn.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chethan.abn.*
import com.chethan.demoproject.utils.LiveDataCallAdapterFactory
import com.chethan.abn.util.LiveDataTestUtil.getValue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNull.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Chethan on 5/3/2019.
 */

@RunWith(JUnit4::class)
class NetworkServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: NetWorkApi

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(NetWorkApi::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getValidSearchResponse() {
        enqueueResponse("valid_search.json")
        val response = (getValue(service.searchVenues(CLIENT_ID,
                CLIENT_SECRET, "Rotterdam", RADIUS, VERSION)) as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/v2/venues/search?client_id=VVLRKMJWUSDI44T0SSZVLQ0FJRRC5ZXPWQAIQSKKPEKRXNDW&client_secret=VTCYNUTZ3LHS31XDYEA0L0KC1ENXHCLIXCLNINJCUOLOMUBP&near=Rotterdam&radius=1000&v=20190503"))

        assertThat<VenueSearchResponse>(response, notNullValue())
        val venue = response.response.venues[0]
        assertThat(venue.location.city, `is`("Rotterdam"))
        assertThat(venue.location.address, `is`("Coolsingel 63"))
     }


    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader
            .getResourceAsStream("api-Response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}
