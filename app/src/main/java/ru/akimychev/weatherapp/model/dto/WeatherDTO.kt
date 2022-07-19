package ru.akimychev.weatherapp.model.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    @SerializedName("fact")
    val fact: FactDTO,
    @SerializedName("forecast")
    val forecast: ForecastDTO,
    @SerializedName("info")
    val info: InfoDTO,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String
): Parcelable