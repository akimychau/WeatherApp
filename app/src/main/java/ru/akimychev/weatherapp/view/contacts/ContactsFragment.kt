package ru.akimychev.weatherapp.view.contacts

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.squareup.picasso.Picasso
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.FragmentContactsBinding
import ru.akimychev.weatherapp.databinding.FragmentDetailsBinding
import ru.akimychev.weatherapp.domain.Weather
import ru.akimychev.weatherapp.viewmodel.details.DetailsFragmentAppState
import ru.akimychev.weatherapp.viewmodel.details.DetailsViewModel

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}