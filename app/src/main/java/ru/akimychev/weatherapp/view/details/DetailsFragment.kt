package ru.akimychev.weatherapp.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.*
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentDetailsBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.viewmodel.details.DetailsFragmentAppState
import ru.akimychev.weatherapp.viewmodel.details.DetailsViewModel

class DetailsFragment : Fragment() {

    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var weatherLocal: Weather

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA_WEATHER)
        weather?.let { weatherLocal ->
            this.weatherLocal = weatherLocal
            viewModel.getWeather(weatherLocal.city.lat, weatherLocal.city.lon)
            viewModel.getLiveData().observe(
                viewLifecycleOwner
            ) { t -> renderData(t) }
        }
    }

    //Способы отображения "Статусов"
    private fun renderData(detailsFragmentAppState: DetailsFragmentAppState) {
        when (detailsFragmentAppState) {
            is DetailsFragmentAppState.Error -> binding.loadingLayout.visibility = View.GONE
            DetailsFragmentAppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
            is DetailsFragmentAppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val weatherDTO = detailsFragmentAppState.weatherData
                with(binding) {
                    cityName.text = weatherLocal.city.name
                    cityCoordinates.text = String.format(
                        getString(R.string.city_coordinates),
                        weatherLocal.city.lat.toString(),
                        weatherLocal.city.lon.toString()
                    )
                    temperatureValue.text = weatherDTO.fact.temp.toString()
                    feelsLikeValue.text = weatherDTO.fact.feelsLike.toString()
                }
            }
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