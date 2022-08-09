package ru.akimychev.weatherapp.model.room

import ru.akimychev.weatherapp.MyApp
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.*

class RepositoryDetailsRoomImpl : RepositoryDetails, RepositoryAddWeather,
    RepositoryHistoryCitiesList {
    override fun getWeather(city: City, callback: AllInOneCallback) {
        callback.onResponse(
            MyApp.getWeatherDatabase().weatherDao().getWeatherByLocation(city.lat, city.lon).let {
                convertHistoryEntityToWeather(it).last()
            })
    }

    override fun addWeather(weather: Weather) {
                MyApp.getWeatherDatabase().weatherDao().insertRoom(convertWeatherToEntity(weather))
    }

    override fun getHistoryWeather(callback: HistoryCitiesCallback) {
        callback.onResponse(
            convertHistoryEntityToWeather(
                MyApp.getWeatherDatabase().weatherDao().getWeatherAll()
            )
        )
    }

    private fun convertHistoryEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(City(it.name, it.lat, it.lon), it.temperature, it.feelsLike)
        }
    }

    private fun convertWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            0,
            weather.city.name + " резерв", weather.city.lat, weather.city.lon, weather.temperature,
            weather.feelsLike
        )
    }
}