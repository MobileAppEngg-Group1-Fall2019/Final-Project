package com.group_01.finalproject.ui.notifications

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
import com.group_01.finalproject.R
import com.group_01.finalproject.db.*
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    var plantNames = arrayListOf<String>()
    var selectedPlantID : Long = -1


    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var dbHelper: DBInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
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
        for (plant in plants){
            plantNames.add(plant.name)
        }
        //plantNames.add("*")
        Log.d("Manish testing",plantNames.toString())

        val adapter = ArrayAdapter(this.context,android.R.layout.simple_spinner_item, plantNames)

        val spinnerPlantNames: Spinner = this.spinner_plantNames

        spinnerPlantNames!!.adapter = adapter

        spinnerPlantNames!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

                //probably do nothing right?
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("Manish testing", "plant id is " + plants[position].plantId.toString())
                Log.d("Manish testing","plant name is " + spinnerPlantNames.selectedItem.toString())
                Log.d("Manish testing", "whatever this id is is " + id.toString())

                selectedPlantID = plants[position].plantId

                val plantPics : ArrayList<ImageModel> = dbHelper.getPlantImages(selectedPlantID)

                val adapter = PhotoAdapter(context!!, R.layout.photo_grid_element, plantPics)
                gridView.adapter = adapter
            }

        }

    }

}