package ru.akimychev.weatherapp.view.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_history_cities_list.*
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentCitiesListBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.utils.SP_DB_KEY
import ru.akimychev.weatherapp.utils.showSnackBar
import ru.akimychev.weatherapp.view.details.DetailsFragment
import ru.akimychev.weatherapp.view.details.OnItemClick
import ru.akimychev.weatherapp.viewmodel.list.CitiesListFragmentAppState
import ru.akimychev.weatherapp.viewmodel.list.CitiesListViewModel

class CitiesListFragment : Fragment(), OnItemClick {
    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentCitiesListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CitiesListViewModel by lazy {
        ViewModelProvider(this)[CitiesListViewModel::class.java]
    }
    private var isMixed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitiesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t ->
            renderData(t)
        }
        binding.citiesListFragmentFAB.setOnClickListener {
            changeWeatherList()
        }
        showListOfTowns()
    }

    private fun renderData(citiesListFragmentAppState: CitiesListFragmentAppState) {
        when (citiesListFragmentAppState) {
            is CitiesListFragmentAppState.Loading -> {
                binding.citiesListFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is CitiesListFragmentAppState.Error -> {
                loadingGone()
                citiesHistoryListFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { changeWeatherList() })
            }
            is CitiesListFragmentAppState.Success -> {
                loadingGone()
                binding.citiesListFragmentRecyclerView.adapter =
                    CitiesListAdapter(citiesListFragmentAppState.weatherListData, this)
            }
        }
    }

    private fun loadingGone() {
        binding.citiesListFragmentLoadingLayout.visibility = View.GONE
    }

    private fun showListOfTowns() {
        if (requireActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(SP_DB_KEY, false)) {
            changeWeatherList()
        } else {
            viewModel.getWeatherListForWorld()
        }
    }

    private fun changeWeatherList() {
        with(viewModel) {
            if (isMixed) {
                getWeatherListForWorld()
            } else {
                getWeatherListForRussia()
            }
        }
        isMixed = !isMixed
        with(requireActivity().getPreferences(Context.MODE_PRIVATE).edit()) {
            putBoolean(SP_DB_KEY, isMixed)
            apply()
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather)).addToBackStack("")
            .commit()
    }

    companion object {
        fun newInstance() = CitiesListFragment()
    }
}