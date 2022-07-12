package ru.akimychev.weatherapp.utils

import com.google.gson.Gson
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.view.details.OnResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object WeatherLoader {
    /*fun requestFirstVariant(lat: Double, lon: Double, onResponse: OnResponse) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        var myConnection: HttpURLConnection? = null

        myConnection = uri.openConnection() as HttpURLConnection
        myConnection.readTimeout = 5000
        myConnection.addRequestProperty("X-Yandex-API-Key", "ceae3d76-b634-4bfd-8ef5-25a327758ae9")
        Thread {
            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
            val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
            onResponse.onResponse(weatherDTO)
        }.start()
    }*/

    fun requestSecondVariant(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        val myConnection = uri.openConnection().apply { readTimeout = 5000 } as HttpURLConnection
        with(myConnection){
            addRequestProperty("X-Yandex-API-Key", "ceae3d76-b634-4bfd-8ef5-25a327758ae9")
            Thread {
                val reader = BufferedReader(InputStreamReader(inputStream))
                val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
                block(weatherDTO)
            }.start()
        }
    }
}