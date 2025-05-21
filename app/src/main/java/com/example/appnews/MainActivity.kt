package com.example.appnews

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.appnews.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var toolbar: Toolbar
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
//    lateinit var mHome: TabItem
//    lateinit var mSports: TabItem
//    lateinit var mHealth: TabItem
//    lateinit var mScience: TabItem
//    lateinit var mTechnology: TabItem
//    lateinit var mEntertainment: TabItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        tabLayout = findViewById(R.id.tablaout)
        viewPager = findViewById(R.id.fragment_container)
//        mHome = findViewById(R.id.home)
//        mSports = findViewById(R.id.sports)
//        mHealth = findViewById(R.id.health)
//        mScience = findViewById(R.id.science)
//        mEntertainment = findViewById(R.id.entertainment)
//        mTechnology = findViewById(R.id.technology)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 6)
        viewPager.adapter = viewPagerAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.currentItem = tab.position
                    if (tab.position == 0 || tab.position == 1 || tab.position == 2 || tab.position == 3 || tab.position == 4 || tab.position == 5) {
                        viewPagerAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        viewPager.addOnPageChangeListener(object:TabLayout.TabLayoutOnPageChangeListener(tabLayout){})
    }
}