package com.ametszc.nearplaces

import com.ametszc.domain.Place

fun PlacesResponseServer.toPlaceDomainList(): List<Place> = results.map {
    it.run {
        Place(
            name,
            formattedAddress ?: "Invalid",
            geometry.location
        )
    }
}