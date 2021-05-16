package com.yazlab.mobilsorgular

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_frg_giris.*

class frgGiris : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frg_giris, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tip1.setOnClickListener{
            val action = frgGirisDirections.actionFrgGirisToFrgTip1()
            Navigation.findNavController(it).navigate(action)
        }

        tip2.setOnClickListener{
            val action = frgGirisDirections.actionFrgGirisToFrgTip2()
            Navigation.findNavController(it).navigate(action)
        }

        tip3.setOnClickListener{
            val action = frgGirisDirections.actionFrgGirisToFrgTip3()
            Navigation.findNavController(it).navigate(action)
        }
    }
}