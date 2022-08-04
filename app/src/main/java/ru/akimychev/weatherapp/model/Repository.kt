package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.Location
import java.io.IOException

fun interface RepositoryDetails {
    fun getWeather(city: City, callback: AllInOneCallback)
}

fun interface RepositoryAddWeather {
    fun addWeather(weather: Weather)
}

fun interface RepositoryCitiesList {
    fun getCitiesList(location: Location): List<Weather>
}

interface AllInOneCallback {
    fun onResponse(weather: Weather)
    fun onFailure(e: IOException)
}

fun interface RepositoryHistoryCitiesList {
    fun getHistoryWeather(callback: HistoryCitiesCallback)
}

interface HistoryCitiesCallback {
    fun onResponse(weather: List<Weather>)
    fun onFailure(e: IOException)
}