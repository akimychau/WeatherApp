package ru.akimychev.weatherapp.viewmodel

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
            liveData.postValue(AppState.Success(Any()))
        }.start()
    }
}