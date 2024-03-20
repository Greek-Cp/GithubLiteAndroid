package com.yanuar.githubliteandroid.data.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yanuar.githubliteandroid.ui.fragment.ListUserFragment

class TabsPagerAdapter(fragment: Fragment, private val username: String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = ListUserFragment.newInstance()
        fragment.arguments = Bundle().apply {
            putInt("section_number", position)
            putString("username", username)
        }
        return fragment
    }
}
