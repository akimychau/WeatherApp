package ru.akimychev.weatherapp.model.details.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.akimychev.weatherapp.BuildConfig
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.model.AllInOneCallback
import ru.akimychev.weatherapp.model.RepositoryDetails
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.bindDtoWithCity
import java.io.IOException

class RepositoryDetailsRetrofitImpl : RepositoryDetails {
    override fun getWeather(city: City, callback: AllInOneCallback) {
        val api = retrofitImpl.build().create(WeatherAPI::class.java)
        api.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onResponse(bindDtoWithCity(response.body()!!, city))
                    } else {
                        callback.onFailure(IOException("403"))
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    callback.onFailure(t as IOException)
                }

            })
    }
    companion object {
        val retrofitImpl = Retrofit.Builder().apply { baseUrl("https://api.weather.yandex.ru")
            addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create())
            ) }
    }
}