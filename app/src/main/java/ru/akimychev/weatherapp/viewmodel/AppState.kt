package ru.akimychev.weatherapp.viewmodel

import ru.akimychev.weatherapp.domain.Weather

sealed class AppState {
    data class SuccessSingle(val weatherData: Weather) : AppState()
    data class SuccessList(val weatherListData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
