package ru.akimychev.weatherapp.utils

sealed class Location {
    object Russia : Location()
    object World : Location()
}
