package ru.akimychev.weatherapp.model

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.domain.getRussianCities
import ru.akimychev.weatherapp.domain.getWorldCities
import ru.akimychev.weatherapp.utils.Location

class RepositoryCitiesListImpl : RepositoryCitiesList {
    override fun getCitiesList(location: Location): List<Weather> {
        return when (location) {
            Location.Russia -> getRussianCities()
            Location.World -> getWorldCities()
        }
    }
}