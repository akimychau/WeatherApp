package ru.akimychev.weatherapp

import android.app.Application
import androidx.room.Room
import ru.akimychev.weatherapp.model.room.WeatherDatabase
import ru.akimychev.weatherapp.utils.ROOM_DB_KEY

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        myApp = this
    }

    companion object {
        private var myApp: MyApp? = null
        private var weatherDatabase: WeatherDatabase? = null
        private fun getMyApp() = myApp!!
        fun getWeatherDatabase(): WeatherDatabase {
                if (weatherDatabase == null) {
                    weatherDatabase =
                        Room.databaseBuilder(getMyApp(), WeatherDatabase::class.java, ROOM_DB_KEY)
                            .allowMainThreadQueries()
                            .build()
                }
            return weatherDatabase!!
        }
    }
}