package com.ametszc.technical_test.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ametszc.technical_test.ui.FavoritePlacesFragment
import com.ametszc.technical_test.ui.NearestPlacesFragment

class HomeNavigationStateAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val homeStateFragmentList: List<Fragment> = listOf(
        NearestPlacesFragment.newInstance(),
        FavoritePlacesFragment.newInstance()
    )

    override fun getItemCount(): Int = homeStateFragmentList.size

    override fun createFragment(position: Int): Fragment = homeStateFragmentList[position]
}
