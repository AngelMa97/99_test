package com.ametszc.usecases

import com.ametszc.data.PlacesRepository
import com.ametszc.domain.Place
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class GetNearestPlaces(
    private val placesRepository: PlacesRepository
) {
    fun invoke(): Single<List<Place>> = placesRepository.getNearestPlaces()
}

/*class GetFavoritePlaces(
    private val placesRepository: PlacesRepository
) {
    fun invoke(): Flowable<List<Any>> = placesRepository.getFavoritePlaces()
}

class UpdateFavoritePlaces(
    private val placesRepository: PlacesRepository
) {
    fun invoke(place: Any): Maybe<Boolean> = placesRepository.updateFavoritePlace(place)
}*/