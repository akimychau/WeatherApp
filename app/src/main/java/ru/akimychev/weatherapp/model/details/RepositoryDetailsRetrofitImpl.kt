package ru.akimychev.weatherapp.model.details

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.RepositoryDetails

class RepositoryDetailsRetrofitImpl : RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double): Weather {
        TODO("Not yet implemented")
    }
}