package ru.akimychev.weatherapp.viewmodel

import ru.akimychev.weatherapp.domain.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
