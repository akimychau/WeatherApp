package ru.akimychev.weatherapp.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.akimychev.weatherapp.databinding.FragmentCitiesListRecyclerItemBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.view.details.OnItemClick

class CitiesListAdapter(private val dataList: List<Weather>, private val callback: OnItemClick) :
    RecyclerView.Adapter<CitiesListAdapter.WeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            FragmentCitiesListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            FragmentCitiesListRecyclerItemBinding.bind(itemView).apply {
                cityName.text = weather.city.name
                root.setOnClickListener {
                    callback.onItemClick(weather)
                }
            }
        }
    }
}