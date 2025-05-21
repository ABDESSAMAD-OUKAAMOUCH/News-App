package com.example.appnews.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appnews.fragments.EntertainmentFragment
import com.example.appnews.fragments.HealthFragment
import com.example.appnews.fragments.HomeFragment
import com.example.appnews.fragments.ScienceFragment
import com.example.appnews.fragments.SportsFragment
import com.example.appnews.fragments.TechnologyFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, val tabCount: Int)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SportsFragment()
            2 -> HealthFragment()
            3 -> ScienceFragment()
            4 -> EntertainmentFragment()
            else -> TechnologyFragment()
        }
    }
}
