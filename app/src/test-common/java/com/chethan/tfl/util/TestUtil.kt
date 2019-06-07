package com.chethan.abn.util

import com.chethan.abn.api.VenueDetailsResponse
import com.chethan.abn.api.VenueSearchResponse
import com.chethan.abn.model.*


/**
 * Created by Chethan on 5/3/2019.
 */
object TestUtil {


    val venueIds = arrayListOf("5a2285eddee7701b1d63d2d3",
            "4b83cb72f964a520d71031e3",
            "57051b94cd109e9534173678",
            "5ba3b6552aff31002c73af4f",
            "590f0fc782a750569d14ade6",
            "5ab8ecc112f0a910328064de",
            "4d6ff0919aac224b2b6434ed",
            "578b85c6498e6cad2b5424d4",
            "4be1736c0f03a593164a19b4",
            "4ec376860aafcf9a808cd4d1")

    fun getVenueItem(): Venue {
        return createVenueItem(
                "5a2285eddee7701b1d63d2d3",
                "Trainmore",
                "Coolsingel 63",
                "NL",
                "Rotterdam",
                "South Holland",
                "Netherlands",
                "3012 AS"
        )
    }

    fun getVenueDetailItem(): VenueDetails {
        return TestUtil.createVenueDetailItem(
                "5a2285eddee7701b1d63d2d3",
                "Café Van Zuylen",
                "De Hair + make up salon van Gert",
                "31206391055",
                "+31206391055",
                7.7,
                "Torensteeg 4-8",
                "NL",
                "Amsterdam",
                "Noord-Holland",
                "Nederland",
                "1012 TH")
    }

    fun getListOfVenueIds(): List<String> {
        return venueIds
    }

    fun createVenueSearchResponse(item: Venue): VenueSearchResponse {
        val meta = Meta("200", "5cd430eb6a60712128752e38")
        val venues: List<Venue> = createVenueArrayList(item)
        val response = VenueSearchResponse.Response(venues)
        return VenueSearchResponse(meta, response)
    }

    fun createVenueDetailsResponse(item: VenueDetails): VenueDetailsResponse {
        val meta = Meta("200", "5cd430eb6a60712128752e38")
        val venueDetails: VenueDetails = createVenueDetails(item)
        val response = DetailsResponse(venueDetails)
        return VenueDetailsResponse(meta, response)
    }

    fun createVenueDetailsWithPhotos(item: VenueDetails): VenueDetailsWithPhotos {
        val venuesPhotos: List<VenuePhotos> = listOfImageUrls()
        val venusDetails = createVenueDetails(item)
        return VenueDetailsWithPhotos(venusDetails, venuesPhotos)
    }

    fun createVenueDetails(item: VenueDetails): VenueDetails {
        return createVenueDetailItem(
                item.id!!,
                item.name!!,
                item.description!!,
                item.contact?.phone!!,
                item.contact?.formattedPhone!!,
                item.rating!!,
                item.location?.address!!,
                item.location?.cc!!,
                item.location?.city!!,
                item.location?.state!!,
                item.location?.country!!,
                item.location?.postalCode!!
        )
    }


    private fun listOfImageUrls(): List<VenuePhotos> {
        val venuePhotos1 = VenuePhotos("4a26ff57f964a520257f1fe3", "https://fastly.4sqi.net/img/general/612x612/1726997_hCCghvcG_kvbUb8gf7LDdfxGPyr29N9_bUWxEY-bJGw.jpg")
        val venuePhotos2 = VenuePhotos("4a26ff57f964a520257f1fe3", "https://fastly.4sqi.net/img/general/612x612/1726997_hCCghvcG_kvbUb8gf7LDdfxGPyr29N9_bUWxEY-bJGw.jpg")
        val list = ArrayList<VenuePhotos>()
        list.add(venuePhotos1)
        list.add(venuePhotos2)
        return list;
    }

    fun createVenueArrayList(item: Venue): List<Venue> {
        val list: ArrayList<Venue> = arrayListOf<Venue>()
        for (venueId in venueIds) {
            list.add(
                    createVenueItem(
                            venueId!!,
                            item.name!!,
                            item.location.address!!,
                            item.location.cc!!,
                            item.location.city!!,
                            item.location.state!!,
                            item.location.country!!,
                            item.location.postalCode!!
                    )
            )
        }

        return list

    }


    fun createVenueItem(
            id: String,
            name: String,
            locationAddress: String,
            locationCC: String,
            locationCity: String,
            locationState: String,
            locationCountry: String,
            locationPostalCode: String
    ) = Venue(
            id = id,
            name = name,
            location = Location(
                    locationAddress,
                    locationCC,
                    locationCity,
                    locationState,
                    locationCountry,
                    locationPostalCode
            )
    )


    fun createVenueDetailItem(
            id: String,
            name: String,
            description: String,
            phone: String,
            formattedPhone: String,
            rating: Double,
            locationAddress: String,
            locationCC: String,
            locationCity: String,
            locationState: String,
            locationCountry: String,
            locationPostalCode: String
    ) = VenueDetails(

            id = id,
            name = name,
            description = description,
            contact = Contact(phone, formattedPhone),
            rating = rating,
            location = Location(
                    locationAddress,
                    locationCC,
                    locationCity,
                    locationState,
                    locationCountry,
                    locationPostalCode
            ),
            photos = Photos(0, emptyList())

    )


}
