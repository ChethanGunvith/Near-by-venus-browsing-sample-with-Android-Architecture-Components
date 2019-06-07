package com.chethan.abn.api


import com.chethan.abn.model.Meta
import com.chethan.abn.model.Venue
import java.io.Serializable

/**
 * Simple object to hold venues search responses. This is different from the Entity in the database
 * because we are keeping a search result in 1 row and denormalizing list of results into a single
 * column.
 */
data class VenueSearchResponse(
        val meta: Meta,
        val response: Response
) : Serializable {

    data class Response(
            val venues: List<Venue>
    ) : Serializable {

    }
}