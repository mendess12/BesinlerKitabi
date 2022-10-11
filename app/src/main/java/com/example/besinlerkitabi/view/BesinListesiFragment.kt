package com.example.besinlerkitabi.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.besinlerkitabi.R
import com.example.besinlerkitabi.adapter.BesinRecyclerAdapter
import com.example.besinlerkitabi.viewmodel.BesinListesiViewModel
import kotlinx.android.synthetic.main.fragment_besin_listesi.*


class BesinListesiFragment : Fragment() {

    private lateinit var viewModel: BesinListesiViewModel
    private val recyclerBesinAdapter = BesinRecyclerAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_besin_listesi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(BesinListesiViewModel::class.java)
        viewModel.refreshData()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerBesinAdapter

        swipeRefreshLayout.setOnRefreshListener {
            besinErrorMessage.visibility = View.GONE
            recyclerView.visibility = View.GONE
            besinProgressBar.visibility = View.VISIBLE
            viewModel.refreshFromInternet()
            swipeRefreshLayout.isRefreshing = false
        }

        observerLiveData()

    }

    fun observerLiveData() {

        viewModel.besinler.observe(viewLifecycleOwner, Observer { besinler ->

            besinler?.let {
                recyclerView.visibility = View.VISIBLE
                recyclerBesinAdapter.besinListesiniGuncelle(besinler)
            }
        })

        viewModel.besinHataMesaji.observe(viewLifecycleOwner, Observer { hata ->

            hata?.let {

                if (it) {
                    besinErrorMessage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    besinProgressBar.visibility = View.GONE
                } else {
                    besinErrorMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    besinProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.besinYukleniyor.observe(viewLifecycleOwner, Observer { yukleniyor ->

            yukleniyor?.let {
                if (it) {
                    besinErrorMessage.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    besinProgressBar.visibility = View.VISIBLE
                } else {
                    besinErrorMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    besinProgressBar.visibility = View.GONE
                }
            }
        })
    }

}