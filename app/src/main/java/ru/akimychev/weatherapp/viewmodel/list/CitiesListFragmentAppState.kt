package ru.akimychev.weatherapp.viewmodel.list

import ru.akimychev.weatherapp.domain.Weather

sealed class CitiesListFragmentAppState {
    data class Success(val weatherListData: List<Weather>) : CitiesListFragmentAppState()
    data class Error(val error: Throwable) : CitiesListFragmentAppState()
    object Loading : CitiesListFragmentAppState()
}
