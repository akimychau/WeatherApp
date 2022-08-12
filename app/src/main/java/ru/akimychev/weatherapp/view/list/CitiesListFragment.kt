package ru.akimychev.weatherapp.view.list

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_history_cities_list.*
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentCitiesListBinding
import ru.akimychev.weatherapp.domain.City
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
        binding.citiesListFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
        showListOfTowns()
    }

    lateinit var locationManager: LocationManager

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    200L,
                    0F,
                    locationListener
                )
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(p0: Location) {
            Log.d("@@@", "${p0.latitude} ${p0.longitude}")
            getAddress(p0)
        }
    }

    private fun checkPermission() {
        val permResult =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showDialog()
        } else {
            permissionRequest(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val REQUEST_CODE_LOCATION = 999

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                    Log.d("@@@", "Получили")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к местоположению")
            .setMessage("Для определения местонахождения требуется предоставить доступ")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                permissionRequest(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("Отклонить") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getAddress(location: Location) {
        val geocoder = Geocoder(context)
        Thread {
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            locationManager.removeUpdates(locationListener)
            onItemClick(
                Weather(
                    City(
                        address.first().locality,
                        location.latitude,
                        location.longitude
                    )
                )
            )
        }.start()
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