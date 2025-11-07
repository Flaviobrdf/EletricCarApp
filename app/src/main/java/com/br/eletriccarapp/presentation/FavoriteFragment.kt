package com.br.eletriccarapp.presentation

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.br.eletriccarapp.R
import kotlinx.coroutines.selects.SelectInstance


class FavoriteFragment: Fragment() {

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstance: Bundle?
    ): View?{
        return inflater.inflate(R.layout.favorite_fragment,container, false)
    }
}