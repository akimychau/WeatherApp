package ru.akimychev.weatherapp.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Insert
    fun insertRoom(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_entity_table WHERE lat=:mLat AND lon=:mLon")
    fun getWeatherByLocation(mLat: Double, mLon: Double): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table")
    fun getWeatherAll(): List<WeatherEntity>
}