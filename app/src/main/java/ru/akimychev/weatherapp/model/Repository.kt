package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.utils.Location

fun interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double): Weather
}

fun interface RepositoryCitiesList {
    fun getCitiesList(location: Location): List<Weather>
}