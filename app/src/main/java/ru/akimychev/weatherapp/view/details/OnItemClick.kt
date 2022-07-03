package ru.akimychev.weatherapp.view.details

import ru.akimychev.weatherapp.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}