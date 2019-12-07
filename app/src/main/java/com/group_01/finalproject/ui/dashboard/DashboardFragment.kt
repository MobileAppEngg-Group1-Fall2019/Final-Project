package com.group_01.finalproject.ui.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.group_01.finalproject.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel


    // Setting up recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private var plantList: ArrayList<Plant> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)


        plantList.add(Plant("Bob", "Tomato", false, 4, 7))
        plantList.add(Plant("Rosie", "Rose", true, 10, 3))
        plantList.add(Plant("Sunny", "Sunflower", false, 16, 6))
        plantList.add(Plant("Fern", "Succulent", true, 5, 14))


        // Set up adapter with the contact list
        viewManager = LinearLayoutManager(view.context)
        viewAdapter = ListAdapter(plantList)

        // Set up recycler view
        recyclerView = view.findViewById<RecyclerView>(R.id.plantRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        return view
    }
}