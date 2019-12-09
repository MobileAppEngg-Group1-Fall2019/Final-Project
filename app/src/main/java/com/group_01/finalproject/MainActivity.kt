package com.group_01.finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.group_01.finalproject.db.DBInterface
import com.kwabenaberko.openweathermaplib.constants.Lang
import com.kwabenaberko.openweathermaplib.constants.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import java.text.SimpleDateFormat
import java.util.*
import android.location.LocationListener
import android.os.Looper
import android.location.Criteria
import com.group_01.finalproject.db.CareModel
import com.group_01.finalproject.db.PlantModel
import com.group_01.finalproject.db.UserModel
import com.group_01.finalproject.openweather.WeatherService
import android.app.job.JobScheduler




class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBInterface
    var timeZone = TimeZone.getTimeZone("EST")
    var dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE)
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val weatherHelper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        weatherHelper.setUnits(Units.IMPERIAL)
        weatherHelper.setLang(Lang.ENGLISH)
        /* Instantiate db and anything related */
        // dateFormatter.setTimeZone(timeZone)
        dbHelper = DBInterface(this)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // TODO :- vvvvv TEST ONLY, REMOVE FOR FINAL BUILD vvvvv
        val curTime: Date = Calendar.getInstance().time

        val testCare = arrayListOf(
            CareModel(1, 1, curTime, "Caption", true),
            CareModel(2, 1, curTime, "Caption", false),
            CareModel(3, 1, curTime, "Caption", true),
            CareModel(4, 1, curTime, "Caption", true)

        )
        val testPlant = PlantModel(
            1,
            "Bob",
            "Cactus",
            "Alive",
            true,
            1,
            curTime
        )
        // TODO :- ^^^^^ TEST ONLY, REMOVE FOR FINAL BUILD ^^^^^

        if (dbHelper.getAllUsers().size == 0) { // Creates the initial user.
            val currentTime: Date = Calendar.getInstance().time

            val initUser = UserModel(
                1,
                "user_name",
                99999,
                5,
                6,
                5,
                5,
                3,
                currentTime,
                0.0,
                0.0
            )

            dbHelper.insertUser(initUser) // Insert User into database.

            // TODO :- vvvvv TEST ONLY, REMOVE FOR FINAL BUILD vvvvv
            dbHelper.insertPlant(testPlant)
            testCare.forEach {
                dbHelper.insertCare(it)
            }
            // TODO :- ^^^^^ TEST ONLY, REMOVE FOR FINAL BUILD ^^^^^
        }


        // Checks if permissions are granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            // If permission not granted, request permissions
            ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }

        Log.d("#### Job schedule check", isJobSchedulerRunning(this).toString())


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val user:UserModel = dbHelper.getUser(1)
        Log.d("#### Service test", "points: " + user.points)
    }

    fun isJobSchedulerRunning(context: Context): Boolean {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return jobScheduler.allPendingJobs.size > 0
    }

    fun startService() {
        val intent = Intent(this, WeatherService::class.java)
        startService(intent)

    }

    fun stopService() {
        val intent = Intent(this, WeatherService::class.java)
        stopService(intent)
    }

    fun getCurrentLocation() {
        val location: Location?
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (network_enabled && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            == PackageManager.PERMISSION_GRANTED) {

            val locationListener = object : LocationListener {
                lateinit var mlocation: Location
                override fun onLocationChanged(location: Location) {
                    mlocation = location
                    Log.d("#### Location Changes", location.toString())
                    val currentTime: Date = Calendar.getInstance().getTime();
                    if(dbHelper.getAllUsers().size == 0) {
                        val user: UserModel = UserModel(1, "Bobert", 0, 0, 0, 0, 0, 0, currentTime, location.latitude, location.longitude)
                        val userid = dbHelper.insertUser(user)
                    }

                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    Log.d("Status Changed", status.toString())
                }

                override fun onProviderEnabled(provider: String) {
                    Log.d("Provider Enabled", provider)
                }

                override fun onProviderDisabled(provider: String) {
                    Log.d("Provider Disabled", provider)
                }
            }

            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            criteria.powerRequirement = Criteria.POWER_LOW
            criteria.isAltitudeRequired = false
            criteria.isBearingRequired = false
            criteria.isSpeedRequired = false
            criteria.isCostAllowed = true
            criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
            criteria.verticalAccuracy = Criteria.ACCURACY_HIGH

            val looper: Looper? = null
            locationManager.requestSingleUpdate(criteria, locationListener, looper);

        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Get location upon opening app
                    getCurrentLocation()

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
