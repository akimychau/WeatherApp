package ru.akimychev.weatherapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.akimychev.weatherapp.databinding.ActivityMainBinding
import ru.akimychev.weatherapp.view.ConnectivityBroadcastReceiver
import ru.akimychev.weatherapp.view.contacts.ContactsFragment
import ru.akimychev.weatherapp.view.list.CitiesListFragment
import ru.akimychev.weatherapp.view.room.HistoryCitiesListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val receiver = ConnectivityBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Передали layout фрагмента на экран входа в активити
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CitiesListFragment.newInstance()).commit()
        }
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HistoryCitiesListFragment())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
                true
            }
            R.id.menu_contacts -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ContactsFragment())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}