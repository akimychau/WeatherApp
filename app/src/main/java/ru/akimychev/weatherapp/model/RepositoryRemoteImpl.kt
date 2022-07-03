package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather

class RepositoryRemoteImpl : RepositorySingleCityWeather {
    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread {
            Thread.sleep(200L)
        }.start()
        return Weather()
    }
}