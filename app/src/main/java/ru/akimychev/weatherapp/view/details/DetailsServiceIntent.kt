package ru.akimychev.weatherapp.view.details

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.akimychev.weatherapp.BuildConfig
import ru.akimychev.weatherapp.domain.City
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.BUNDLE_CITY_KEY
import ru.akimychev.weatherapp.utils.BUNDLE_WEATHER_DTO_KEY
import ru.akimychev.weatherapp.utils.WAVE
import ru.akimychev.weatherapp.utils.YANDEX_API_KEY
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsServiceIntent : IntentService("IntentService") {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            it.getParcelableExtra<City>(BUNDLE_CITY_KEY)?.let { city ->
                val uri =
                    URL("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
                Thread {
                    val myConnection =
                        uri.openConnection().apply { readTimeout = 5000 } as HttpsURLConnection
                    try {
                        with(myConnection) {
                            addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                            val reader = BufferedReader(InputStreamReader(inputStream))
                            val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
                            LocalBroadcastManager.getInstance(this@DetailsServiceIntent)
                                .sendBroadcast(Intent().apply {
                                    putExtra(BUNDLE_WEATHER_DTO_KEY, weatherDTO)
                                    action = WAVE
                                })
                        }
                    } catch (e: Exception) {

                    } finally {
                        myConnection.disconnect()
                    }
                }.start()
            }
        }
    }
}