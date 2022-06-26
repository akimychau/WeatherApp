package ru.akimychev.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {
    fun getLiveData() = liveData
    fun sendRequest() {
        liveData.value = AppState.Loading
        Thread {
            sleep(2000L)
            val i =
                (0..2).random()//TODO Всегда в одинаковом порядке генерируются числа при запуске. Рандом работает неправильно
            Log.d("@@@", " $i")
            if (i == 2) {
                liveData.postValue(AppState.Error(Throwable()))
            } else {
                liveData.postValue(AppState.Success(Any()))
            }
        }.start()
    }
}