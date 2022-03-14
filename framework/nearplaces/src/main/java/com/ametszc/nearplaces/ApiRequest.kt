package com.ametszc.nearplaces

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.LatLng;
import io.reactivex.Observable
import io.reactivex.Single
import java.io.IOException;

class PlacesRequest {
    fun run(location: LatLng): Single<PlacesResponseServer> {
        val context = GeoApiContext.Builder()
            .apiKey("AIzaSyA1_M1tyw474M2xQpk74v5soZNfjKEp-GU")
            .build()

        return Single.fromObservable(
            Observable.fromArray(
                PlacesResponseServer(
                    PlacesApi.nearbySearchQuery(context, location)
                        .radius(5000)
                        .rankby(RankBy.PROMINENCE)
                        .keyword("cruise")
                        .language("en")
                        .type(PlaceType.RESTAURANT)
                        .await()
                        .results
                        .toList()
                )
            )
        )
    }
}
