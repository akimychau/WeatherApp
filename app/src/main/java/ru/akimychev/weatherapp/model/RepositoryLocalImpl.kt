package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.domain.getRussianCities
import ru.akimychev.weatherapp.domain.getWorldCities

class RepositoryLocalImpl : RepositorySingle, RepositoryList {

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }

    override fun getListWeather(location: Location): List<Weather> {
        return when (location) {
            Location.Russia -> getRussianCities()
            Location.World -> getWorldCities()
        }
    }
}