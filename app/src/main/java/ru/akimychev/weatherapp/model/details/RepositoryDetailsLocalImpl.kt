package ru.akimychev.weatherapp.model.details

import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.domain.getRussianCities
import ru.akimychev.weatherapp.domain.getWorldCities
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.dto.FactDTO
import ru.akimychev.weatherapp.model.dto.WeatherDTO

class RepositoryDetailsLocalImpl : RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double, callback: AllInOneCallback) {
        val list = getWorldCities().toMutableList()
        list.addAll(getRussianCities())
        val response = list.filter { it.city.lat == lat && it.city.lon == lon }
        callback.onResponse(convertModelToDTO(response.first()))
    }

    private fun convertModelToDTO(weather: Weather): WeatherDTO {
        val fact = FactDTO(weather.feelsLike, weather.temperature)
        return WeatherDTO(fact)
    }
}