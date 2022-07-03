package ru.akimychev.weatherapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.akimychev.weatherapp.R
import ru.akimychev.weatherapp.databinding.ActivityMainBinding
import ru.akimychev.weatherapp.view.list.WeatherListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Передали layout фрагмента на экран входа в активити
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }
    }
}