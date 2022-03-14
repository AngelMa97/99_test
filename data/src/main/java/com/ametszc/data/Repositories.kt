package com.ametszc.data

import com.ametszc.domain.Place
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class PlacesRepository(
    private val remotePlacesDataSource: RemotePlacesDataSource,
    //private val localPlacesDataSource: LocalPlacesDataSource
) {
    fun getNearestPlaces(): Single<List<Place>> = remotePlacesDataSource.getPlaces()

    /*fun getFavoritePlaces(): Flowable<List<Place>> = localPlacesDataSource.getFavoritePlaces()

    fun updateFavoritePlace(place: Place): Maybe<Boolean> =
        localPlacesDataSource.updateFavoritePlaces(place)*/
}