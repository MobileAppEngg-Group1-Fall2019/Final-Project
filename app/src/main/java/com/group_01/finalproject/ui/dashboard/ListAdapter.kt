package com.group_01.finalproject.ui.dashboard


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group_01.finalproject.R

// Entry data structure to store title and description
data class Plant(val name: String, val type: String, val indoors: Boolean, val ageInWeeks: Int, val daysBetweenWatering: Int)

class ListAdapter (private val list: ArrayList<Plant>)
    : RecyclerView.Adapter<ViewHolder>() {

    var onItemClick: ((Plant) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = list[position]
        holder.bind(name)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}

class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {

    private var nameTypeView: TextView? = null
    private var locationAgeView: TextView? = null
    private var waterView: TextView? = null

    init {
        nameTypeView = itemView.findViewById(R.id.plant_name_type)
        locationAgeView = itemView.findViewById(R.id.plant_location_age)
        waterView = itemView.findViewById(R.id.plant_water)
    }

    fun bind(plant: Plant) {
        nameTypeView?.text = plant.name + ", " + plant.type
        if (plant.indoors) {
            locationAgeView?.text = "indoors, " + plant.ageInWeeks + " weeks old"
        } else {
            locationAgeView?.text = "outdoors, " + plant.ageInWeeks + " weeks old"
        }
        waterView?.text = "water every " + plant.daysBetweenWatering + " days"
    }
}
