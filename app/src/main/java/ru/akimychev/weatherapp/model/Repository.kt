package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather

fun interface RepositorySingle {
    fun getWeather(lat: Double, lon: Double): Weather
}

fun interface RepositoryList {
    fun getListWeather(location: Location): List<Weather>
}

sealed class Location {
    object Russia : Location()
    object World : Location()
}