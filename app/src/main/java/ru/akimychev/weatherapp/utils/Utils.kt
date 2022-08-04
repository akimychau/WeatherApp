package ru.akimychev.weatherapp.utils

import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.dto.FactDTO
import ru.akimychev.weatherapp.model.dto.WeatherDTO

fun convertModelToDTO(weather: Weather): WeatherDTO {
    val fact = FactDTO(weather.feelsLike, weather.temperature)
    return WeatherDTO(fact)
}

fun bindDtoWithCity(weatherDTO: WeatherDTO, city: City): Weather {
    val fact: FactDTO = weatherDTO.fact
    return Weather(city, fact.temp, fact.feelsLike)
}