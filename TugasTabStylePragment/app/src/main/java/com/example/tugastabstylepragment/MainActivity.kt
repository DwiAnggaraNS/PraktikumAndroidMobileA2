package com.example.tugastabstylepragment

import SectionPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    companion object {
        // Array berisi ID resource untuk string yang digunakan sebagai judul tab
        private val TAB_TITLES = arrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Toolbar dan set sebagai Action Bar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }
}