package ru.akimychev.weatherapp.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_weather_list.*
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentWeatherListBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.utils.showSnackBar
import ru.akimychev.weatherapp.view.details.DetailsFragment
import ru.akimychev.weatherapp.view.details.OnItemClick
import ru.akimychev.weatherapp.viewmodel.list.CitiesListFragmentAppState
import ru.akimychev.weatherapp.viewmodel.list.CitiesListViewModel

class CitiesListFragment : Fragment(), OnItemClick {
    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CitiesListViewModel by lazy {
        ViewModelProvider(this)[CitiesListViewModel::class.java]
    }
    private var isMixed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(
            viewLifecycleOwner
        ) { t ->  //Подписали VM на обновления View (Связка LD/LO)
            renderData(t)
        }

        binding.weatherListFragmentFAB.setOnClickListener {
            whichListToChoose()
        }
        viewModel.getWeatherListForWorld()
    }

    //Способы отображения "Статусов"
    private fun renderData(citiesListFragmentAppState: CitiesListFragmentAppState) {
        when (citiesListFragmentAppState) {
            is CitiesListFragmentAppState.Loading -> {
                binding.weatherListFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is CitiesListFragmentAppState.Error -> {
                loadingGone()
                weatherListFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { whichListToChoose() })
            }
            is CitiesListFragmentAppState.Success -> {
                loadingGone()
                binding.weatherListFragmentRecyclerView.adapter =
                    CitiesListAdapter(citiesListFragmentAppState.weatherListData, this)
            }
        }
    }

    private fun loadingGone() {
        binding.weatherListFragmentLoadingLayout.visibility = View.GONE
    }

    private fun whichListToChoose() {
        isMixed = !isMixed
        with(viewModel) {
            if (isMixed) {
                getWeatherListForWorld()
            } else {
                getWeatherListForRussia()
            }
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather)).addToBackStack("")
            .commit()
    }

    //Возвращает фрагмент
    companion object {
        fun newInstance() = CitiesListFragment()
    }
}