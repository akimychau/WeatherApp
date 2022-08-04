package ru.akimychev.weatherapp.view.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentHistoryCitiesListBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.view.details.DetailsFragment
import ru.akimychev.weatherapp.view.details.OnItemClick
import ru.akimychev.weatherapp.view.list.CitiesListAdapter
import ru.akimychev.weatherapp.viewmodel.historyList.CitiesHistoryListFragmentAppState
import ru.akimychev.weatherapp.viewmodel.historyList.CitiesHistoryListViewModel

class HistoryCitiesListFragment : Fragment(), OnItemClick {
    //Инициировали ViewBinding и раздули во фрагменте
    private var _binding: FragmentHistoryCitiesListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CitiesHistoryListViewModel by lazy {
        ViewModelProvider(this)[CitiesHistoryListViewModel::class.java]
    }
    private var isMixed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryCitiesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t ->
            renderData(t)
        }
        viewModel.getAllHistory()
    }

    private fun renderData(citiesHistoryListFragmentAppState: CitiesHistoryListFragmentAppState) {
        when (citiesHistoryListFragmentAppState) {
            is CitiesHistoryListFragmentAppState.Loading -> {
            }
            is CitiesHistoryListFragmentAppState.Error -> {

            }
            is CitiesHistoryListFragmentAppState.Success -> {
                binding.citiesHistoryListFragmentRecyclerView.adapter =
                    CitiesListAdapter(citiesHistoryListFragmentAppState.weatherListData, this)
            }
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather)).addToBackStack("")
            .commit()
    }

    companion object {
        fun newInstance() = HistoryCitiesListFragment()
    }
}