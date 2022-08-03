package ru.akimychev.weatherapp.viewmodel.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.model.RepositoryCitiesList
import ru.akimychev.weatherapp.model.RepositoryCitiesListImpl
import ru.akimychev.weatherapp.utils.Location

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
        liveData.value =
            CitiesListFragmentAppState.Success(repositoryCitiesList.getCitiesList(location))
    }

    private fun isConnection(): Boolean {
        return false
    }
}