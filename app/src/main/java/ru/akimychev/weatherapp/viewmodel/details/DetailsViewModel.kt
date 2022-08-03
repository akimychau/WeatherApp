package ru.akimychev.weatherapp.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.details.RepositoryDetailsLocalImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsOkHttpImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsWeatherLoaderImpl
import ru.akimychev.weatherapp.model.details.retrofit.RepositoryDetailsRetrofitImpl
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import java.io.IOException

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData(),
) :
    ViewModel() {
    lateinit var repositoryDetails: RepositoryDetails
    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        choiceRepository()
        return liveData
    }

    //Выбор репозитория в зависимости от подключения к серверу
    private fun choiceRepository() {
        repositoryDetails = when (2) {
            1 -> RepositoryDetailsOkHttpImpl()
            2 -> RepositoryDetailsRetrofitImpl()
            3 -> RepositoryDetailsWeatherLoaderImpl()
            else -> RepositoryDetailsLocalImpl()
        }
    }

    //Идет запрос
    fun getWeather(lat: Double, lon: Double) {
        choiceRepository()
        liveData.value = DetailsFragmentAppState.Loading
        repositoryDetails.getWeather(lat, lon, callback)
    }

    private val callback = object : AllInOneCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            liveData.postValue(DetailsFragmentAppState.Success(weatherDTO))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DetailsFragmentAppState.Error(e))
        }

    }
}

private fun isConnection(): Boolean {
    return false
}