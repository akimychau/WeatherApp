package ru.akimychev.weatherapp.viewmodel.historyList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.HistoryCitiesCallback
import ru.akimychev.weatherapp.model.room.RepositoryDetailsRoomImpl
import java.io.IOException

class CitiesHistoryListViewModel(
    private val liveData: MutableLiveData<CitiesHistoryListFragmentAppState> = MutableLiveData(),
) :
    ViewModel() {
    lateinit var repositoryDetailsRoom: RepositoryDetailsRoomImpl
    fun getLiveData(): MutableLiveData<CitiesHistoryListFragmentAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repositoryDetailsRoom = RepositoryDetailsRoomImpl()
    }

    fun getAllHistory() {
        repositoryDetailsRoom.getHistoryWeather(callback)
    }

    private val callback = object : HistoryCitiesCallback {
        override fun onResponse(weatherList: List<Weather>) {
            liveData.postValue(CitiesHistoryListFragmentAppState.Success(weatherList))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(CitiesHistoryListFragmentAppState.Error(e))
        }
    }
}