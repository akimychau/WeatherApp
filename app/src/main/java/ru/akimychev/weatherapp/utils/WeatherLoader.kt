package ru.akimychev.weatherapp.utils

import com.google.gson.Gson
import ru.akimychev.weatherapp.BuildConfig
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object WeatherLoader {

    fun requestSecondVariant(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        Thread {
            val myConnection =
                uri.openConnection().apply { readTimeout = 5000 } as HttpsURLConnection
            try {
                with(myConnection) {
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
                    block(weatherDTO)
                }
            } catch (e: Exception) {

            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}