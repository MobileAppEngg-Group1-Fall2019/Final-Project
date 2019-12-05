package com.group_01.finalproject

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.group_01.finalproject.db.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBInterface
    var timeZone = TimeZone.getTimeZone("EST")
    var dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Instantiate db and anything related */
        // dateFormatter.setTimeZone(timeZone)
        dbHelper = DBInterface(this)

        /*
         Basic db tests - left them in for reference for now
         val currentTime: Date = Calendar.getInstance().getTime();
         Log.d("Time format: ", currentTime.toString())
         val idOne = dbHelper.insertUser(UserModel(1, "John", 0, 0, 0, 0, 0, 0))

         val user: UserModel = dbHelper.getUser(idOne)
         Log.d("#### DB Test", user.name)


         val idTwo = dbHelper.insertPlant(PlantModel(1, "planty", "tomato", "livin", true, 5))
         val idThree = dbHelper.insertImage(ImageModel(1, idTwo, "/", currentTime))

         val plant: PlantModel = dbHelper.getPlant(idTwo)
         Log.d("#### DB Test", plant.name)
         val image: ImageModel = dbHelper.getImage(idThree)
         Log.d("#### DB Test", image.location + ", " + image.lastModified)

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
}
