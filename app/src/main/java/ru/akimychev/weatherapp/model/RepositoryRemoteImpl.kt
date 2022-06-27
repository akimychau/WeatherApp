package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather

class RepositoryRemoteImpl : Repository {
    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread {
            Thread.sleep(200L)
        }.start()
        return Weather()
    }

    override fun getListWeather(): List<Weather> {
        Thread {
            Thread.sleep(300L)
        }.start()
        return listOf(Weather())
    }
}