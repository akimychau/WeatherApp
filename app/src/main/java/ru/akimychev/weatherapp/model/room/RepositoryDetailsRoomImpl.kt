package ru.akimychev.weatherapp.model.room

import ru.akimychev.weatherapp.MyApp
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryAddWeather
import ru.akimychev.weatherapp.model.RepositoryDetails

class RepositoryDetailsRoomImpl : RepositoryDetails, RepositoryAddWeather {
    override fun getWeather(weather: Weather, callback: AllInOneCallback) {
        callback.onResponse(
            MyApp.getWeatherDatabase().weatherDao().getWeatherByLocation(weather.city.lat, weather.city.lon).let {
                convertHistoryEntityToWeather(it).last()
            })
    }

    override fun addWeather(weather: Weather) {
        MyApp.getWeatherDatabase().weatherDao().insertRoom(convertWeatherToEntity(weather))
    }

    private fun convertHistoryEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(City(it.name, it.lat, it.lon), it.temperature, it.feelsLike)
        }
    }

    private fun convertWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            0,
            weather.city.name+" резерв", weather.city.lat, weather.city.lon, weather.temperature,
            weather.feelsLike
        )
    }
}