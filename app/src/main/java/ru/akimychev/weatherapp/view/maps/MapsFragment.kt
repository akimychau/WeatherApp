package ru.akimychev.weatherapp.view.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentMapsUiBinding

class MapsFragment : Fragment() {
    lateinit var map: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


        googleMap.setOnMapLongClickListener { latLng ->
            addMarkerToArray(latLng)
            setMarker(latLng, "", R.drawable.ic_map_marker)
            drawLine()
        }

        googleMap.uiSettings.isZoomControlsEnabled = true

        checkPermission()
        googleMap.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }

    private fun checkPermission() {
        val permResult =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            getMyLocation()
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
                    getMyLocation()
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

    private var _binding: FragmentMapsUiBinding? = null
    private val binding: FragmentMapsUiBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsUiBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            binding.searchAddress.text.toString().let { searchText ->
                val geocoder = Geocoder(requireContext())
                val result = geocoder.getFromLocationName(searchText, 1)
                val ln = LatLng(result.first().latitude, result.first().longitude)
                setMarker(ln, searchText, R.drawable.ic_map_marker)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ln, 15f))


            }
        }
    }

    private val markers = mutableListOf<Marker>()
    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }


    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(15f)
            )
        }
    }


    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )!!
    }

}