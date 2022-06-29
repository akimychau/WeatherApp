package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather

class RepositoryRemoteImpl : RepositorySingle {
    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread {
            Thread.sleep(200L)
        }.start()
        return Weather()
    }
}