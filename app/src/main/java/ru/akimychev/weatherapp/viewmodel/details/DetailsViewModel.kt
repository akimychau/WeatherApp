package ru.akimychev.weatherapp.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryAddWeather
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.details.RepositoryDetailsLocalImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsOkHttpImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsWeatherLoaderImpl
import ru.akimychev.weatherapp.model.details.retrofit.RepositoryDetailsRetrofitImpl
import ru.akimychev.weatherapp.model.room.RepositoryDetailsRoomImpl
import java.io.IOException

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData(),
) :
    ViewModel() {
    lateinit var repositoryDetails: RepositoryDetails
    lateinit var repositoryAddWeather: RepositoryAddWeather

    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        choiceRepository()
        return liveData
    }

    //Выбор репозитория в зависимости от подключения к серверу
    private fun choiceRepository() {
        if (isConnection()) {
            repositoryDetails = when (2) {
                1 -> RepositoryDetailsOkHttpImpl()
                2 -> RepositoryDetailsRetrofitImpl()
                else -> RepositoryDetailsWeatherLoaderImpl()
            }
            repositoryAddWeather = RepositoryDetailsRoomImpl()
        } else {
            repositoryDetails = when (1) {
                1 -> RepositoryDetailsRoomImpl()
                else -> RepositoryDetailsLocalImpl()
            }
            repositoryAddWeather = RepositoryDetailsRoomImpl()
        }
    }

    //Идет запрос
    fun getWeather(city: City) {
        choiceRepository()
        liveData.value = DetailsFragmentAppState.Loading
        repositoryDetails.getWeather(city, callback)
    }

    private val callback = object : AllInOneCallback {
        override fun onResponse(weather: Weather) {
            if (isConnection()) repositoryAddWeather.addWeather(weather)
            liveData.postValue(DetailsFragmentAppState.Success(weather))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DetailsFragmentAppState.Error(e))
        }
    }
}

private fun isConnection(): Boolean {
    return false
}