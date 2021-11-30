package com.finject.instagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.finject.instagram.R

class FavouriteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, parent, false)
        return view
    }
}