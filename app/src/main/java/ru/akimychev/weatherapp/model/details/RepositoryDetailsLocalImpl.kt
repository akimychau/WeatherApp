package ru.akimychev.weatherapp.model.details

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.domain.getRussianCities
import ru.akimychev.weatherapp.domain.getWorldCities
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryDetails

class RepositoryDetailsLocalImpl : RepositoryDetails {
    override fun getWeather(weather: Weather, callback: AllInOneCallback) {
        val list = getWorldCities().toMutableList()
        list.addAll(getRussianCities())
        val response = list.filter { it.city.lat == weather.city.lat && it.city.lon == weather.city.lon }
        callback.onResponse((response.first()))
    }
}