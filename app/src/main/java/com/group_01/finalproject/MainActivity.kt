package com.group_01.finalproject

import android.Manifest
import android.content.Context
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
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBInterface
    var timeZone = TimeZone.getTimeZone("EST")
    var dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE)

    val PERMISSIONS_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val helper = OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY))
        /* Instantiate db and anything related */
        // dateFormatter.setTimeZone(timeZone)
        dbHelper = DBInterface(this)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        ActivityCompat.requestPermissions(this, permissions,PERMISSIONS_LOCATION)

        helper.getCurrentWeatherByCityName("Accra", object : CurrentWeatherCallback {
            override fun onSuccess(currentWeather: CurrentWeather) {
                Log.v(
                    "#### API Test",
                    "Coordinates: " + currentWeather.coord.lat + ", " + currentWeather.coord.lon + "\n"
                            + "Weather Description: " + currentWeather.weather[0].description + "\n"
                            + "Temperature: " + currentWeather.main.tempMax + "\n"
                            + "Wind Speed: " + currentWeather.wind.speed + "\n"
                            + "City, Country: " + currentWeather.name + ", " + currentWeather.sys.country
                )
            }

            override fun onFailure(throwable: Throwable) {
                Log.v("#### API Test", throwable.message)
            }
        })

        /*
        //Basic db tests - left them in for reference for now
        var imageSample: Bitmap = DbBitmapUtil.imageURLToBitmap("https://picsum.photos/200/300")

        var imageView: ImageView = findViewById<ImageView>(R.id.imageView)

        val currentTime: Date = Calendar.getInstance().getTime();
        Log.d("Time format: ", currentTime.toString())
        val idOne = dbHelper.insertUser(UserModel(1, "John", 0, 0, 0, 0, 0, 0))

        val user: UserModel = dbHelper.getUser(idOne)
        Log.d("#### DB Test", user.name)


        val idTwo = dbHelper.insertPlant(PlantModel(1, "planty", "tomato", "livin", true, 5))
        val idThree = dbHelper.insertImage(ImageModel(0, 1, DbBitmapUtil.getBytes(imageSample), currentTime))

        val plant: PlantModel = dbHelper.getPlant(idTwo)
        Log.d("#### DB Test", plant.name)
        val image: ImageModel = dbHelper.getImage(idThree)
        Log.d("#### DB Test", "" + image.lastModified)
       imageView.setImageBitmap(DbBitmapUtil.getImage(image.data))
        */

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
    }

    fun getCurrentLocation(locManager: LocationManager, network_enabled: Boolean) {
        val location: Location?

        if (network_enabled && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
            == PackageManager.PERMISSION_GRANTED) {

            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            if (location != null) {
                longitude = location.longitude
                latitude = location.latitude
                Log.i("Location", "$longitude + $latitude")
            }
            Log.i("Location", "BAAAAD")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Get location upon opening app
                    val locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    getCurrentLocation(locManager, network_enabled)

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
