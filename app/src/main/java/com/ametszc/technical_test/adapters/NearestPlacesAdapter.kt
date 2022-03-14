package com.ametszc.technical_test.adapters

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ametszc.domain.Place
import com.ametszc.technical_test.R
import com.ametszc.technical_test.databinding.NearestPlacesItemBinding
import com.ametszc.technical_test.util.bindingInflate


class NearestPlacesAdapter(
    private val listener: (Place) -> Unit
): RecyclerView.Adapter<NearestPlacesAdapter.NearestPlacesViewHolder>() {

    private val placesList: MutableList<Place> = mutableListOf()

    fun addData(newData: List<Place>) {
        placesList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearestPlacesViewHolder =
        NearestPlacesViewHolder(
            parent.bindingInflate(R.layout.nearest_places_item, false),
            listener
        )

    override fun onBindViewHolder(holder: NearestPlacesViewHolder, position: Int) {
        holder.bind(placesList[position])
    }

    override fun getItemCount(): Int = placesList.size

    class NearestPlacesViewHolder(
        private val databinding: NearestPlacesItemBinding,
        private val listener: (Place) -> Unit
    ): RecyclerView.ViewHolder(databinding.root) {

        fun bind(item: Place) {
            databinding.place = item
            itemView.setOnClickListener { listener(item) }
        }
    }

}
