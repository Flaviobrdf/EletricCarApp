package com.br.eletriccarapp.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.br.eletriccarapp.presentation.ui.CarFragment

class TabAdapter(fragment: MainActivity) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
         return when (position) {
             0 -> CarFragment()
             1 -> FavoriteFragment()
             else -> CarFragment()

        }
    }

    override fun getItemCount(): Int {
        return 2
    }




}