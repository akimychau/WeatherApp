package ru.akimychev.weatherapp.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.details.RepositoryDetailsLocalImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsOkHttpImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsRetrofitImpl
import ru.akimychev.weatherapp.model.details.RepositoryDetailsWeatherLoaderImpl

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
        repositoryDetails = when (1) {
            1 -> RepositoryDetailsOkHttpImpl()
            2 -> RepositoryDetailsRetrofitImpl()
            3 -> RepositoryDetailsWeatherLoaderImpl()
            else -> RepositoryDetailsLocalImpl()
        }
    }

    //Идет запрос
    fun getWeather(lat: Double, lon: Double) {
        liveData.value = DetailsFragmentAppState.Loading
        liveData.value = DetailsFragmentAppState.Error(IllegalStateException("Что-то пошло не так"))
        liveData.value = DetailsFragmentAppState.Success(repositoryDetails.getWeather(lat, lon))
    }
}

private fun isConnection(): Boolean {
    return false
}