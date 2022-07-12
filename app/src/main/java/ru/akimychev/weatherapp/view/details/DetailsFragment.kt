package ru.akimychev.weatherapp.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentDetailsBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.WeatherLoader

class DetailsFragment : Fragment() {

    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA_WEATHER)
        weather?.let { weatherLocal ->
            /*WeatherLoader.requestFirstVariant(
                weatherLocal.city.lat,
                weatherLocal.city.lon
            ) { weatherDTO ->
                bindLocalValuesUpdatedFromServer(weatherLocal, weatherDTO)
            }*/
            WeatherLoader.requestSecondVariant(
                weatherLocal.city.lat,
                weatherLocal.city.lon
            ) { weatherDTO ->
                bindLocalValuesUpdatedFromServer(weatherLocal, weatherDTO)

            }
        }
    }

    private fun bindLocalValuesUpdatedFromServer(
        weatherLocal: Weather,
        weatherDTO: WeatherDTO
    ) {
        requireActivity().runOnUiThread {
            renderData(weatherLocal.apply {
                weatherLocal.feelsLike = weatherDTO.fact.feelsLike
                weatherLocal.temperature = weatherDTO.fact.temp
            })
        }
    }

    //Способы отображения "Статусов"
    private fun renderData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weather.city.lat.toString(),
                weather.city.lon.toString()
            )
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Возвращает фрагмент
    companion object {
        const val BUNDLE_EXTRA_WEATHER = "BUNDLE_EXTRA_WEATHER"
        fun newInstance(weather: Weather): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(BUNDLE_EXTRA_WEATHER, weather)
            }
            return fragment
        }
    }
}