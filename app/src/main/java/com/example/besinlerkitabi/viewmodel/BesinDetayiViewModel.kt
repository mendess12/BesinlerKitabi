package com.example.besinlerkitabi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.besinlerkitabi.model.Besin

class BesinDetayiViewModel : ViewModel() {

    val besinLiveData = MutableLiveData<Besin>()

    fun getRoomData(){
        val muz = Besin("Muz", "100", "10", "5", "1", "www.test.com")
        besinLiveData.value = muz
    }

}