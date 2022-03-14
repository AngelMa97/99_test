package com.ametszc.technical_test.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ametszc.domain.Place
import com.ametszc.technical_test.presentation.NearestPlacesViewModel.NearestPlaces.*
import com.ametszc.technical_test.presentation.util.Event
import com.ametszc.usecases.GetNearestPlaces
import com.google.android.libraries.places.api.model.PlaceLikelihood
import io.reactivex.disposables.CompositeDisposable

class NearestPlacesViewModel(
    private val getPlacesUseCase: GetNearestPlaces
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<NearestPlaces>>()
    val events: LiveData<Event<NearestPlaces>> get() = _events

    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onGetPlaces() {
        disposable.add(
            getPlacesUseCase
                .invoke()
                .doOnSubscribe { showLoader() }
                .subscribe(
                    { placesList ->
                        hideLoader()
                        if (placesList.size > 0) {
                            _events.postValue(Event(ShowPlacesList(placesList)))
                        } else {
                            _events.postValue(Event((ShowNotPlacesFound)))
                        }
                    }, { error ->
                        hideLoader()
                        _events.postValue(Event(ShowPlacesError(error)))
                    }
                )
        )
    }

    private fun showLoader() {
        isLoading = true
        _events.value = Event(ShowLoader)
    }

    private fun hideLoader() {
        isLoading = false
        _events.value = Event(HideLoader)
    }

    sealed class NearestPlaces {
        data class ShowPlacesError(val error: Throwable) : NearestPlaces()
        data class ShowPlacesList(val places: List<Place>) : NearestPlaces()
        object ShowNotPlacesFound : NearestPlaces()
        object HideLoader : NearestPlaces()
        object ShowLoader : NearestPlaces()
    }
}