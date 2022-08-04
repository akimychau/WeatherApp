package ru.akimychev.weatherapp.viewmodel.historyList

import ru.akimychev.weatherapp.domain.Weather

sealed class CitiesHistoryListFragmentAppState {
    data class Success(val weatherListData: List<Weather>) : CitiesHistoryListFragmentAppState()
    data class Error(val error: Throwable) : CitiesHistoryListFragmentAppState()
    object Loading : CitiesHistoryListFragmentAppState()
}
