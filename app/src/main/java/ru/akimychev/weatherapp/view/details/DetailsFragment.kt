package ru.akimychev.weatherapp.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.akimychev.weatherapp.BuildConfig
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentDetailsBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.model.dto.WeatherDTO
import ru.akimychev.weatherapp.utils.BUNDLE_WEATHER_DTO_KEY
import ru.akimychev.weatherapp.utils.YANDEX_API_KEY

class DetailsFragment : Fragment() {

    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(BUNDLE_WEATHER_DTO_KEY)?.let { weatherDTO ->
                    bindLocalValuesUpdatedFromServer(weatherLocal, weatherDTO)
                }
            }
        }
    }
    lateinit var weatherLocal: Weather

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
            this.weatherLocal = weatherLocal
            /*LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(receiver, IntentFilter(WAVE))

            requireActivity().startService(Intent(requireContext(), DetailsServiceIntent::class.java).apply {
                putExtra(BUNDLE_CITY_KEY, weatherLocal.city)
            })*/
            val client = OkHttpClient()
            val builder = Request.Builder()
            builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            builder.url("https://api.weather.yandex.ru/v2/informers?lat=${weatherLocal.city.lat}&lon=${weatherLocal.city.lon}")
            val request: Request = builder.build()
            val call: Call = client.newCall(request)
            Thread{
                val response = call.execute()
                if (response.isSuccessful){
                    response.body()?.let {
                        val weatherDTO = Gson().fromJson(it.string(), WeatherDTO::class.java)
                        weatherLocal.feelsLike = weatherDTO.fact.feelsLike
                        weatherLocal.temperature = weatherDTO.fact.temp
                        requireActivity().runOnUiThread(){
                            renderData(weatherLocal)
                        }
                    }
                }
            }.start()
        }
    }

    private fun bindLocalValuesUpdatedFromServer(
        weatherLocal: Weather,
        weatherDTO: WeatherDTO
    ) {
        renderData(weatherLocal.apply {
            weatherLocal.feelsLike = weatherDTO.fact.feelsLike
            weatherLocal.temperature = weatherDTO.fact.temp
        })
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
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
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