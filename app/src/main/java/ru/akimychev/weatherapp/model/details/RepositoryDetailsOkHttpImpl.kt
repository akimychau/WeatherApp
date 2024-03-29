package ru.akimychev.weatherapp.model.details

import com.google.gson.Gson
import okhttp3.*
import ru.akimychev.weatherapp.BuildConfig
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.YANDEX_API_KEY
import ru.akimychev.weatherapp.utils.bindDtoWithCity
import java.io.IOException

class RepositoryDetailsOkHttpImpl : RepositoryDetails {
    override fun getWeather(city: City, callback: AllInOneCallback) {
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
        val request: Request = builder.build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && response.body() != null) {
                    val weatherDTO =
                        Gson().fromJson(response.body()?.string(), WeatherDTO::class.java)
                    callback.onResponse(bindDtoWithCity(weatherDTO, city))
                } else {
                    callback.onFailure(IOException("403"))
                }
            }
        })
    }
}