package ru.akimychev.weatherapp.model.details.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.RETROFIT_GET
import ru.akimychev.weatherapp.utils.RETROFIT_LAT
import ru.akimychev.weatherapp.utils.RETROFIT_LON
import ru.akimychev.weatherapp.utils.YANDEX_API_KEY

interface WeatherAPI {
    @GET(RETROFIT_GET)
    fun getWeather(
        @Header(YANDEX_API_KEY) keyValue: String,
        @Query(RETROFIT_LAT) lat: Double,
        @Query(RETROFIT_LON) lon: Double
    ): Call<WeatherDTO>
}