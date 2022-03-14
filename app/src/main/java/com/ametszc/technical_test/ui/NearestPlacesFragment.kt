package com.ametszc.technical_test.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ametszc.data.PlacesRepository
import com.ametszc.data.RemotePlacesDataSource
import com.ametszc.domain.Place
import com.ametszc.nearplaces.NearestPlacesDataSource
import com.ametszc.technical_test.R
import com.ametszc.technical_test.adapters.NearestPlacesAdapter
import com.ametszc.technical_test.presentation.NearestPlacesViewModel
import com.ametszc.usecases.GetNearestPlaces
import com.google.maps.model.LatLng
import com.ametszc.technical_test.util.getViewModel
import kotlinx.android.synthetic.main.fragment_nearest_places.*
import java.lang.ClassCastException

class NearestPlacesFragment : Fragment() {

    private lateinit var nearestPlacesAdapter: NearestPlacesAdapter
    private lateinit var listener: OnPlaceSelected

    private val remotePlacesDataSource: RemotePlacesDataSource by lazy {
        MainActivity.locationCompanion?.let {
            NearestPlacesDataSource(it)
        } ?: NearestPlacesDataSource(LatLng(-33.8670522, 151.1957362))
    }

    private val placesRepository: PlacesRepository by lazy {
        PlacesRepository(remotePlacesDataSource)
    }

    private val getNearestPlaces: GetNearestPlaces by lazy {
        GetNearestPlaces(placesRepository)
    }

    private val nearestPlacesViewModel: NearestPlacesViewModel by lazy {
        getViewModel {
            NearestPlacesViewModel(
                getNearestPlaces
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnPlaceSelected
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnPlaceSelected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearest_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setViews()

        nearestPlacesViewModel.onGetPlaces()
    }

    private fun setViews() {
        nearestPlacesAdapter = NearestPlacesAdapter { place ->
            listener.openPlaceDetail(place)
        }.also {
            setHasOptionsMenu(true)
        }
        rvNearestPlaces.run {
            adapter = nearestPlacesAdapter
        }
    }

    private fun setObservers() {
        nearestPlacesViewModel.events.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { navigation ->
                when (navigation) {
                    is NearestPlacesViewModel.NearestPlaces.ShowPlacesList -> navigation.run {
                        nearestPlacesAdapter.addData(places)
                    }
                }
            }
        }
    }

    interface OnPlaceSelected {
        fun openPlaceDetail(place: Place)
    }

    companion object {
        const val TAG = "NearestPlacesFragment::"
        fun newInstance() = NearestPlacesFragment()
    }
}