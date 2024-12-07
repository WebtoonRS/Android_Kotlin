package com.example.webtoon_project.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.webtoon_project.R

class SearchPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> StoryBasedFragment()
            1 -> StyleBasedFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }

    companion object {
        val TAB_TITLES = arrayOf(
            R.string.tab_story,
            R.string.tab_style
        )
    }
}