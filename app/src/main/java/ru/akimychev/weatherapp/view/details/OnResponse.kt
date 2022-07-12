package ru.akimychev.weatherapp.view.details

import ru.akimychev.weatherapp.model.dto.WeatherDTO

fun interface OnResponse {
    fun onResponse(weather: WeatherDTO)
}