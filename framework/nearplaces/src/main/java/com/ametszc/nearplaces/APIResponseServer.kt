package com.ametszc.nearplaces

import com.ametszc.nearplaces.APIConstants.KEY_RESULTS
import com.google.gson.annotations.SerializedName
import com.google.maps.model.PlacesSearchResult

data class PlacesResponseServer(
    @SerializedName(KEY_RESULTS) val results: List<PlacesSearchResult>
)