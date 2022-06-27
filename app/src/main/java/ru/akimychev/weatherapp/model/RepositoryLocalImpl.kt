package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather

class RepositoryLocalImpl : Repository {

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }

    override fun getListWeather(): List<Weather> {
        return listOf(Weather())
    }
}