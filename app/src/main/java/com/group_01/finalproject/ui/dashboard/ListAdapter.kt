package com.group_01.finalproject.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.group_01.finalproject.R
import com.group_01.finalproject.db.CareModel
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.DbBitmapUtil
import com.group_01.finalproject.db.PlantModel
import java.util.*

class ListAdapter (private val list: ArrayList<PlantModel>, private val dBHelper: DBInterface)
    : RecyclerView.Adapter<ViewHolder>() {

    var onItemClick: ((PlantModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = list[position]
        holder.bind(plant)

        holder.itemView.setOnLongClickListener{ view ->
            // Update last watered plant
            val currentTime: Date = Calendar.getInstance().time
            val updatedPlant = PlantModel(plant.plantId, plant.name, plant.type, plant.status, plant.indoor, plant.age, currentTime)
            val newCare = CareModel(0, plant.plantId, currentTime, "caption", true)
            dBHelper.updatePlant(updatedPlant)
            dBHelper.insertCare(newCare)
            list.remove(plant)
            list.add(updatedPlant)
            this.notifyDataSetChanged()
            Toast.makeText(view.context, "Thank you for watering ${plant.name}!", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun getItemCount(): Int = list.size
}

class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {

    private var nameTypeView: TextView? = null
    private var locationAgeView: TextView? = null
    private var waterView: TextView? = null
    private var plantImageView: ImageView? = null
    private var dbHelper: DBInterface

    init {
        nameTypeView = itemView.findViewById(R.id.plant_name_type)
        locationAgeView = itemView.findViewById(R.id.plant_location_age)
        waterView = itemView.findViewById(R.id.plant_water)
        plantImageView = itemView.findViewById(R.id.plantImageView)

        dbHelper = DBInterface(context = parent.context)

    }

    fun bind(plant: PlantModel) {
        nameTypeView?.text = plant.name + ", " + plant.type
        var weekString = "weeks"
        if (plant.age == 1) {
            weekString = "week"
        }
        if (plant.indoor) {
            locationAgeView?.text = "Location: indoors\nAge: " + plant.age + " $weekString old"
        } else {
            locationAgeView?.text = "Location: outdoors\nAge: " + plant.age + " $weekString old"
        }
        waterView?.text = "Last watered on: " + plant.lastCare.toLocaleString()

        var imageList = dbHelper.getPlantImages(plant.plantId)
        if (imageList.isNotEmpty()) {
            plantImageView?.setImageBitmap(DbBitmapUtil.getImage(imageList[0].data))
        }
    }
}

