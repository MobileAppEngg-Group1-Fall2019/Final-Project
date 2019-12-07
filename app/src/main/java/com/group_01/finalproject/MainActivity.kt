package com.group_01.finalproject

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
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
import android.os.StrictMode




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

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

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
}
