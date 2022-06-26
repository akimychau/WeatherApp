package ru.akimychev.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.akimychev.weatherapp.databinding.FragmentWeatherListBinding

class WeatherListFragment : Fragment() {
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, true)
        val view = binding.root
        return view
    }
}