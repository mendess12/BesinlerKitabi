package com.example.besinlerkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.besinlerkitabi.R
import com.example.besinlerkitabi.util.installImage
import com.example.besinlerkitabi.util.makePlaceholder
import com.example.besinlerkitabi.viewmodel.BesinDetayiViewModel
import kotlinx.android.synthetic.main.fragment_besin_detayi.*

class BesinDetayiFragment : Fragment() {

    private lateinit var viewModel: BesinDetayiViewModel
    private var besinId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_detayi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            besinId = BesinDetayiFragmentArgs.fromBundle(it).besinId
        }

        viewModel = ViewModelProviders.of(this).get(BesinDetayiViewModel::class.java)
        viewModel.getRoomData(besinId)
        observeLiveData()



        include.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

    }

    fun observeLiveData() {
        viewModel.besinLiveData.observe(viewLifecycleOwner, Observer { besin ->
            besin?.let {
                besinIsim.text = besin.isim
                besinKalorisi.text = besin.kalori
                besinKarbonhidrat.text = besin.karbonhidrat
                besinProtein.text = besin.protein
                besinYag.text = besin.yag
                context?.let {
                    besinImage.installImage(besin.gorsel, makePlaceholder(it))
                }
            }
        })
    }
}