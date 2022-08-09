package ru.akimychev.weatherapp.model.details

import com.google.gson.Gson
import ru.akimychev.weatherapp.BuildConfig
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.YANDEX_API_KEY
import ru.akimychev.weatherapp.utils.bindDtoWithCity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryDetailsWeatherLoaderImpl : RepositoryDetails {
    override fun getWeather(city: City, callback: AllInOneCallback) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
        Thread {
            val myConnection =
                uri.openConnection().apply { readTimeout = 5000 } as HttpsURLConnection
            try {
                with(myConnection) {
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
                    callback.onResponse(bindDtoWithCity(weatherDTO, city))
                }
            } catch (e: IOException) {
                callback.onFailure(e)
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}