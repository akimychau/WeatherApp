package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.utils.Location

fun interface RepositorySingleCityWeather {
    fun getWeather(lat: Double, lon: Double): Weather
}

fun interface RepositoryListCitiesWeather {
    fun getListWeather(location: Location): List<Weather>
}