package ru.akimychev.weatherapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentDetailsBinding
import ru.akimychev.weatherapp.domain.Weather

class DetailsFragment : Fragment() {

    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA_WEATHER)
        if (weather != null) {
            renderData(weather)
        }
    }

    //Способы отображения "Статусов"
    private fun renderData(weather: Weather) {
        setData(weather)
    }

    //Обработка данных
    private fun setData(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weather.city.lat.toString(),
            weather.city.lon.toString()
        )
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Возвращает фрагмент
    companion object {
        const val BUNDLE_EXTRA_WEATHER = "BUNDLE_EXTRA_WEATHER"
        fun newInstance(weather: Weather): DetailsFragment {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_EXTRA_WEATHER, weather)
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}