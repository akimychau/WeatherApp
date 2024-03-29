package ru.akimychev.weatherapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import ru.akimychev.weatherapp.databinding.ActivityMainBinding
import ru.akimychev.weatherapp.view.ConnectivityBroadcastReceiver
import ru.akimychev.weatherapp.view.contacts.ContactsFragment
import ru.akimychev.weatherapp.view.list.CitiesListFragment
import ru.akimychev.weatherapp.view.maps.MapsFragment
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("@@@", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("@@@", "$token")
        })
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
            R.id.menu_maps -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapsFragment())
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