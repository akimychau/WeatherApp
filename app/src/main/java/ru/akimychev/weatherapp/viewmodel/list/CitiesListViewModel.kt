package ru.akimychev.weatherapp.viewmodel.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.model.RepositoryCitiesList
import ru.akimychev.weatherapp.model.RepositoryCitiesListImpl
import ru.akimychev.weatherapp.utils.Location
import java.lang.Thread.sleep
import kotlin.random.Random

class CitiesListViewModel(
    private val liveData: MutableLiveData<CitiesListFragmentAppState> = MutableLiveData(),
) :
    ViewModel() {
    lateinit var repositoryCitiesList: RepositoryCitiesList
    fun getLiveData(): MutableLiveData<CitiesListFragmentAppState> {
        choiceRepository()
        return liveData
    }

    //Выбор репозитория в зависимости от подключения к серверу
    private fun choiceRepository() {
        RepositoryCitiesListImpl()
        repositoryCitiesList = RepositoryCitiesListImpl()
    }

    fun getWeatherListForRussia() {
        sendRequest(Location.Russia)
    }

    fun getWeatherListForWorld() {
        sendRequest(Location.World)
    }

    //Идет запрос
    private fun sendRequest(location: Location) {
        liveData.value = CitiesListFragmentAppState.Loading
        Thread {
            sleep(1000L)
            val rand = Random(System.nanoTime())
            if ((0..5).random(rand) == 1) {
                liveData.postValue(CitiesListFragmentAppState.Error(IllegalStateException("Что-то пошло не так")))
            } else {
                liveData.postValue(
                    CitiesListFragmentAppState.Success(
                        repositoryCitiesList.getCitiesList(
                            location
                        )
                    )
                )
            }
        }.start()
    }

    private fun isConnection(): Boolean {
        return false
    }
}