package ru.akimychev.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.model.Repository
import ru.akimychev.weatherapp.model.RepositoryLocalImpl
import ru.akimychev.weatherapp.model.RepositoryRemoteImpl
import kotlin.random.Random

class WeatherListViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
) :
    ViewModel() {
    lateinit var repository: Repository
    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repository = if (isConnection()) {
            RepositoryRemoteImpl()
        } else {
            RepositoryLocalImpl()
        }
    }

    //Идет запрос
    fun sendRequest() {
        liveData.value = AppState.Loading
        val rand = Random(System.nanoTime())
        if ((0..3).random(rand) == 1) {
            liveData.value = AppState.Error(IllegalStateException("Что-то пошло не так"))
        } else {
            liveData.value =
                AppState.Success(
                    repository.getWeather(
                        55.755826,
                        37.617299900000035
                    )
                )
        }
    }

    private fun isConnection(): Boolean {
        return false
    }
}