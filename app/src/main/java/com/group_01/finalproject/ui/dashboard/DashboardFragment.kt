package com.group_01.finalproject.ui.dashboard


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.PlantModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    var timeZone = TimeZone.getTimeZone("EST")
    var dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


    // Setting up recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var plantList: ArrayList<PlantModel> = ArrayList()
    var selectedType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val view = inflater.inflate(com.group_01.finalproject.R.layout.fragment_dashboard, container, false)

        val dbHelper = DBInterface(context = this.context!!)

        val addPlantButton = view.findViewById<Button>(com.group_01.finalproject.R.id.addPlantButton)
        addPlantButton.setOnClickListener {
            // custom dialog
            val dialog = Dialog(context)
            dialog.setContentView(com.group_01.finalproject.R.layout.dialog)
            dialog.setTitle("Add New Plant")

            val dropdown = dialog.findViewById(com.group_01.finalproject.R.id.spinnerPlantTypes) as Spinner
            val nameEdit = view.findViewById(com.group_01.finalproject.R.id.name_value) as EditText
            val ageEdit = view.findViewById(com.group_01.finalproject.R.id.age_value) as EditText

            val indoorsSwitch = view.findViewById(com.group_01.finalproject.R.id.indoorSwitch) as Switch

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter.createFromResource(
                context,
                com.group_01.finalproject.R.array.type_list,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                dropdown.adapter = adapter
            }

            dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedType = parent?.selectedItem.toString()
                }
            }

            val dialogCancelButton = dialog.findViewById(com.group_01.finalproject.R.id.dialogCancel) as Button
            // if button is clicked, close the custom dialog
            dialogCancelButton.setOnClickListener{
                    dialog.dismiss()
            }

            val dialogAddButton = dialog.findViewById(com.group_01.finalproject.R.id.dialogAdd) as Button
            // if button is clicked, close the custom dialog
            dialogAddButton.setOnClickListener{
                val name = nameEdit.text.toString()
                val ageText = ageEdit.text
                val type = selectedType
                if (name.isNotEmpty() && ageText.isNotEmpty() && type.isNotEmpty()) {
                    val age = Integer.parseInt(ageText.toString())
                    val currentTime: Date = Calendar.getInstance().getTime()
                    var plantModel = PlantModel(-1, name, type, "healthy", indoorsSwitch.isChecked, age, currentTime)
                    dbHelper.insertPlant(plantModel)
                }
                dialog.dismiss()
            }


            dialog.show()
        }

        plantList = dbHelper.getAllPlants()

        // Set up adapter with the contact list
        viewManager = LinearLayoutManager(view.context)
        viewAdapter = ListAdapter(plantList)

        // Set up recycler view
        recyclerView = view.findViewById<RecyclerView>(com.group_01.finalproject.R.id.plantRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        return view
    }
}