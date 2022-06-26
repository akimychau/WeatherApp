package ru.akimychev.weatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.akimychev.weatherapp.databinding.FragmentWeatherListBinding
import ru.akimychev.weatherapp.viewmodel.AppState
import ru.akimychev.weatherapp.viewmodel.WeatherListViewModel

class WeatherListFragment : Fragment() {
    companion object {
        fun newInstance() = WeatherListFragment()
    }

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
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<AppState> {
            override fun onChanged(t: AppState) {
                Toast.makeText(requireContext(), "Hello $t", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.sendRequest()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}