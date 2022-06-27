package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather

interface Repository {
    fun getWeather(lat: Double, lon: Double): Weather
    fun getListWeather(): List<Weather>
}