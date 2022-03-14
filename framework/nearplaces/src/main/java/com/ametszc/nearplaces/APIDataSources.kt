package com.ametszc.nearplaces

import com.ametszc.data.RemotePlacesDataSource
import com.ametszc.domain.Place
import com.google.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class NearestPlacesDataSource(
    private val location: LatLng
) : RemotePlacesDataSource {

    override fun getPlaces(): Single<List<Place>> {
        return PlacesRequest()
            .run(location)
            .map(PlacesResponseServer::toPlaceDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}
