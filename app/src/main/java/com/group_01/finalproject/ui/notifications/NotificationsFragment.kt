package com.group_01.finalproject.ui.notifications

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.group_01.finalproject.*
import com.group_01.finalproject.db.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import androidx.core.app.ActivityCompat.startActivityForResult
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.provider.MediaStore
import android.R
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*
import kotlin.collections.ArrayList


class NotificationsFragment : Fragment() {

    var plantNames = arrayListOf<String>()
    var plantPics = ArrayList<ImageModel>()
    var selectedPlantID : Long = -1
    var SELECT_PHOTO : Int = 12345

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var dbHelper: DBInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(com.group_01.finalproject.R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(com.group_01.finalproject.R.id.text_notifications)
        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBInterface(context = this.context!!)



        val gridView : GridView = this.photoGrid

        val plants: ArrayList<PlantModel> = dbHelper.getAllPlants()
        plantNames.clear()
        for (plant in plants){
            plantNames.add(plant.name)
        }
        plantNames.add("View All Photos!")
        Log.d("Manish testing",plantNames.toString())

        val adapterForSpinner = ArrayAdapter(this.context,android.R.layout.simple_spinner_item, plantNames)

        val spinnerPlantNames: Spinner = this.spinner_plantNames

        spinnerPlantNames!!.adapter = adapterForSpinner

        spinnerPlantNames!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

                //probably do nothing right?


            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (plantNames[position] != "View All Photos!") {
                    Log.d("Manishtesting", "plant id is " + plants[position].plantId.toString())
                    Log.d(
                        "Manishtesting",
                        "plant name is " + spinnerPlantNames.selectedItem.toString()
                    )
                    Log.d("Manishtesting", "whatever this id is is " + id.toString())

                    selectedPlantID = plants[position].plantId

                    plantPics = dbHelper.getPlantImages(selectedPlantID)

                    val adapterForPhotos = PhotoAdapter(
                        context!!,
                        com.group_01.finalproject.R.layout.photo_grid_element,
                        plantPics
                    )
                    gridView.adapter = adapterForPhotos


                }
                else{
                    plantPics = dbHelper.getAllImages()
                    val adapterForPhotos = PhotoAdapter(
                        context!!,
                        com.group_01.finalproject.R.layout.photo_grid_element,
                        plantPics
                    )
                    gridView.adapter = adapterForPhotos

                    selectedPlantID = -1
                }
            }

        }

        val addImageButton : Button = this.addImageButton
        addImageButton.setOnClickListener{

            Log.d("Manishtesting","pressed the add image button")

            if(selectedPlantID >= 0) {

                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"

                val permission = ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

                if (permission == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO)
                } else {
                    ActivityCompat.requestPermissions(
                        this.activity!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        SELECT_PHOTO
                    )
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO)
                }
            }



        }




    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        //&& resultCode == RESULT_OK
        if (requestCode == SELECT_PHOTO  && data != null) {
            // Let's read picked image data - its URI
            val pickedImage = data.data
            // Let's read picked image path using content resolver
            val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context!!.contentResolver.query(pickedImage, filePath, null, null, null)
            cursor.moveToFirst()
            val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))

            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(imagePath, options)

            // Do something with the bitmap

            var newImageforDB : ImageModel = ImageModel(-1, selectedPlantID,DbBitmapUtil.getBytes(bitmap),
                Calendar.getInstance().getTime())

            Log.d("Manishtesting",selectedPlantID.toString())

            dbHelper.insertImage(newImageforDB)


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close()

            onViewCreated(view!!, Bundle())
        }
    }

}

