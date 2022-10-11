package com.example.besinlerkitabi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.besinlerkitabi.model.Besin
import com.example.besinlerkitabi.service.BesinAPIService
import com.example.besinlerkitabi.service.BesinDatabase
import com.example.besinlerkitabi.util.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class BesinListesiViewModel(application: Application) : BaseViewModel(application) {

    val besinler = MutableLiveData<List<Besin>>()
    val besinHataMesaji = MutableLiveData<Boolean>()
    val besinYukleniyor = MutableLiveData<Boolean>()

    private val besinApiService = BesinAPIService()
    private val disposable = CompositeDisposable()

    private val sharedPreferences = SharedPreferences(getApplication())
    private val guncellemeZamani =
        10 * 60 * 1000 * 1000 * 1000L //10 dakikanın nano zamana çevrilmiş hali

    fun refreshData() {

        val kaydedilmeZamani = sharedPreferences.zamaniAl()
        if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani) {
            //verileri SQLite'dan çek
            getDataFromSqlite()
        } else {
            //verileri sunucudan çek
            getDataFromInternet()
        }
    }

    fun refreshFromInternet(){
        getDataFromInternet()
    }

    private fun getDataFromSqlite(){
        besinYukleniyor.value = true

        launch {
            val besinListesi = BesinDatabase(getApplication()).besinDao().getAllBesin()
            besinleriGoster(besinListesi)
            Toast.makeText(getApplication(),"Veriler SQLite'tan alındı",Toast.LENGTH_LONG).show()
        }
    }


    private fun getDataFromInternet() {
        besinYukleniyor.value = true

        disposable.addAll(
            besinApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Besin>>() {
                    override fun onSuccess(t: List<Besin>) {
                        //success
                        sqliteSakla(t)
                        Toast.makeText(getApplication(),"Veriler sunucudan alındı",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        //error
                        besinHataMesaji.value = true
                        besinYukleniyor.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun besinleriGoster(besinlerListesi: List<Besin>) {
        besinler.value = besinlerListesi
        besinHataMesaji.value = false
        besinYukleniyor.value = false
    }

    private fun sqliteSakla(besinListesi: List<Besin>) {

        launch {
            val dao = BesinDatabase(getApplication()).besinDao()
            dao.deleteAllBesin()
            val uuidListesi = dao.insertAll(*besinListesi.toTypedArray())
            var i = 0
            while (i < besinListesi.size) {
                besinListesi[i].uuid = uuidListesi[i].toInt()
                i = i + 1
            }
            besinleriGoster(besinListesi)
        }

        sharedPreferences.zamaniKaydet(System.nanoTime())
    }
}