package com.ametszc.data

import com.ametszc.domain.Place
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RemotePlacesDataSource {
    fun getPlaces(): Single<List<Place>>
}

interface LocalPlacesDataSource {
    fun getFavoritePlaces(): Flowable<List<Place>>

    fun updateFavoritePlaces(place: Place): Maybe<Boolean>
}
