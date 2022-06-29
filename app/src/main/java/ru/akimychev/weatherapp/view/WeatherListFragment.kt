package ru.akimychev.weatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.akimychev.weatherapp.databinding.FragmentWeatherListBinding
import ru.akimychev.weatherapp.viewmodel.AppState
import ru.akimychev.weatherapp.viewmodel.WeatherListViewModel

class WeatherListFragment : Fragment() {
    //Возвращает фрагмент
    companion object {
        fun newInstance() = WeatherListFragment()
    }

    var isMixed = true

    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: WeatherListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)//Заварили чан с VM
        viewModel.getLiveData().observe(
            viewLifecycleOwner,
            object : Observer<AppState> { //Подписали VM на обновления View (Связка LD/LO)
                override fun onChanged(t: AppState) {
                    renderData(t)
                }
            })

        binding.weatherListFragmentFAB.setOnClickListener {
            whichListToChoose()
        }
    }

    //Способы отображения "Статусов"
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessSingle -> {
                val weatherData = appState.weatherData
                //setData(weatherData)
                //binding.weatherListFragmentLoadingLayout.visibility = View.GONE
                //  Snackbar.make(binding.root, "Success", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                binding.weatherListFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.weatherListFragmentLoadingLayout.visibility = View.GONE
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                Snackbar.make(binding.root, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") {
                        whichListToChoose()
                    }
                    .show()
            }
            is AppState.SuccessList -> {
                binding.weatherListFragmentLoadingLayout.visibility = View.GONE
                binding.weatherListFragmentRecyclerView.adapter =
                    WeatherListAdapter(appState.weatherListData)
            }
        }
    }

    private fun whichListToChoose() {
        isMixed = !isMixed
        if (isMixed) {
            viewModel.getWeatherListForWorld()
        } else {
            viewModel.getWeatherListForRussia()
        }
    }

    //Обработка данных
    /*private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.name
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}