package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.Location
import java.io.IOException

fun interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double, callback: AllInOneCallback)
}

fun interface RepositoryCitiesList {
    fun getCitiesList(location: Location): List<Weather>
}

interface AllInOneCallback {
    fun onResponse(weatherDTO: WeatherDTO)
    fun onFailure(e: IOException)
}