package ru.akimychev.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.model.*
import java.lang.Thread.sleep
import kotlin.random.Random

class WeatherListViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
) :
    ViewModel() {
    lateinit var repositorySingle: RepositorySingle
    lateinit var repositoryList: RepositoryList
    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    //Выбор репозитория в зависимости от подключения к серверу
    private fun choiceRepository() {
        repositorySingle = if (isConnection()) {
            RepositoryRemoteImpl()
        } else {
            RepositoryLocalImpl()
        }
        repositoryList = RepositoryLocalImpl()
    }

    fun getWeatherListForRussia(){
        sendRequest(Location.Russia)
    }
    fun getWeatherListForWorld(){
        sendRequest(Location.World)
    }

    //Идет запрос
    private fun sendRequest(location: Location) {
        liveData.value = AppState.Loading
        Thread{
            sleep(1000L)
            val rand = Random(System.nanoTime())
            if ((0..5).random(rand) == 1) {
                liveData.postValue(AppState.Error(IllegalStateException("Что-то пошло не так")))
            } else {
                liveData.postValue(AppState.SuccessList(repositoryList.getListWeather(location)))
            }}.start()
    }

    private fun isConnection(): Boolean {
        return false
    }
}