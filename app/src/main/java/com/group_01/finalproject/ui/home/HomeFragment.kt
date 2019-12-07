package com.group_01.finalproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.group_01.finalproject.R
import com.group_01.finalproject.db.CareModel
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.PlantModel
import com.group_01.finalproject.db.UserModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.history_card.view.*
import java.util.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dbHelper: DBInterface
    private var mUser: UserModel? = null
    private var mCare: ArrayList<CareModel>? = null
    private var badgesConsistency = listOf<Int>()
    private var badgesDiversity = listOf<Int>()
    private var badgesPhotos = listOf<Int>()
    private var badgesGreenThumb = listOf<Int>()
    private var badgesOfBadges = listOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbHelper = DBInterface(context = this.context!!)

        val testUser = UserModel(
            1,
            "test_user",
            50123,
            2,
            3,
            4,
            3,
            2,
            Date(2018, 10, 1),
            12.123123,
            13.12312312
        )

        val testCare = arrayListOf(
            CareModel(1, 1,Date(2019,8,7),"Caption",true),
            CareModel(2,1,Date(2019,19,7),"Caption",false),
            CareModel(3,1,Date(2019,10,7),"Caption",true),
            CareModel(4,1,Date(2019,11,7),"Caption",true)
        )
        val currentTime: Date = Calendar.getInstance().getTime();
        val testPlant = PlantModel(
            1,
            "Bob",
            "Cactus",
            "Alive",
            true,
            1,
            currentTime
        )

        dbHelper.insertUser(testUser)

        dbHelper.insertPlant(testPlant)

        testCare.forEach {
            dbHelper.insertCare(it)
        }


        // Get User & Care data from database
        mUser = dbHelper.getUser(1)
        mCare = dbHelper.getAllCare()

        createText(); createBadges(); createHistory()
    }

    // Set TextViews for total points & start date.
    private fun createText() {
        val points = mUser?.points.toString()
        val year = mUser?.creationDate?.year
        val month = mUser?.creationDate?.month
        val day = mUser?.creationDate?.day

        val stringMonth = monthToString(month)

        value_total_points.text = "Total Points: $points"
        value_start_date.text = "Gardening Since $stringMonth $day, $year!"
    }

    // Create and add image view of badges to badges_layout using user db data.
    private fun createBadges() {
        when(mUser?.consistencyBadge){ // CONSISTENCY BADGE  // Range: 0-5
            1 -> {
                addBadge(badgesConsistency[0])
            }
            2 -> {
                for (i in 0..1) { addBadge((badgesConsistency[i])) }
            }
            3 -> {
                for (i in 0..2) { addBadge((badgesConsistency[i])) }
            }
            4 -> {
                for (i in 0..3) { addBadge((badgesConsistency[i])) }
            }
            5 -> {
                for (i in 0..4) { addBadge((badgesConsistency[i])) }
            }
        }
        when(mUser?.diversityBadge){ // DIVERSITY BADGE   //Range: 0-6
            1 -> {
                addBadge(badgesDiversity[0])
            }
            2 -> {
                for (i in 0..1) { addBadge((badgesDiversity[i])) }
            }
            3 -> {
                for (i in 0..2) { addBadge((badgesDiversity[i])) }
            }
            4 -> {
                for (i in 0..3) { addBadge((badgesDiversity[i])) }
            }
            5 -> {
                for (i in 0..4) { addBadge((badgesDiversity[i])) }
            }
            6 -> {
                for (i in 0..5) { addBadge((badgesDiversity[i])) }
            }
        }
        when(mUser?.photosBadge){ // PHOTOGRAPHER BADGE   //Range: 0-5
            1 -> {
                addBadge(badgesPhotos[0])
            }
            2 -> {
                for (i in 0..1) { addBadge((badgesPhotos[i])) }
            }
            3 -> {
                for (i in 0..2) { addBadge((badgesPhotos[i])) }
            }
            4 -> {
                for (i in 0..3) { addBadge((badgesPhotos[i])) }
            }
            5 -> {
                for (i in 0..4) { addBadge((badgesPhotos[i])) }
            }
        }
        when(mUser?.greenThumbBadge){ // GREEN THUMB BADGE   //Range: 0-5
            1 -> {
                addBadge(badgesGreenThumb[0])
            }
            2 -> {
                for (i in 0..1) { addBadge((badgesGreenThumb[i])) }
            }
            3 -> {
                for (i in 0..2) { addBadge((badgesGreenThumb[i])) }
            }
            4 -> {
                for (i in 0..3) { addBadge((badgesGreenThumb[i])) }
            }
            5 -> {
                for (i in 0..4) { addBadge((badgesGreenThumb[i])) }
            }
        }
        when(mUser?.badgeOfBadges){ // BADGE OF BADGES   //Range: 0-3
            1 -> {
                addBadge(badgesOfBadges[0])
            }
            2 -> {
                for (i in 0..1) { addBadge((badgesOfBadges[i])) }
            }
            3 -> {
                for (i in 0..2) { addBadge((badgesOfBadges[i])) }
            }
        }
    }

    private fun createHistory() {
        mCare?.forEach {
            val layoutInflater = LayoutInflater.from(context)
            val plant = dbHelper.getPlant(it.plantID)
            val year = it.date.year
            val month = monthToString(it.date.month)
            val day = it.date.day

            val card: View = layoutInflater.inflate(
                R.layout.history_card,
                history_cards_layout,
                false
            )

            card.card_name.text = plant.name
            card.card_type.text = plant.type
            card.card_date.text = "$month $day, $year"
            card.card_bool.text = if(it.completed) "Yes" else "No" // This is ugly but deal with it!

            history_cards_layout.addView(card)
        }
    }

    private fun addBadge(badge: Int) {
        val mImageView = ImageView(context)
        mImageView.setImageResource(badge)
        badges_layout.addView(mImageView)
    }

    private fun monthToString(month: Int?): String {
        // Month field uses values from 0-11.
        when (month) {
            0 -> return "January"
            1 -> return "February"
            2 -> return "March"
            3 -> return "April"
            4 -> return "May"
            5 -> return "June"
            6 -> return "July"
            7 -> return "August"
            8 -> return "September"
            9 -> return "October"
            10 -> return "November"
            11 -> return "December"
        }
        return ""
    }

    // Create lists containing references to badge locations
    init{
        badgesConsistency = listOf(
            R.mipmap.badge_consistency_1_foreground,
            R.mipmap.badge_consistency_10_foreground,
            R.mipmap.badge_consistency_100_foreground,
            R.mipmap.badge_consistency_1000_foreground,
            R.mipmap.badge_consistency_10000_foreground
        )
        badgesDiversity = listOf(
            R.mipmap.badge_diversity_2_foreground,
            R.mipmap.badge_diversity_4_foreground,
            R.mipmap.badge_diversity_6_foreground,
            R.mipmap.badge_diversity_8_foreground,
            R.mipmap.badge_diversity_10_foreground,
            R.mipmap.badge_diversity_12_foreground
        )
        badgesPhotos = listOf(
            R.mipmap.badge_photographer_1_foreground,
            R.mipmap.badge_photographer_10_foreground,
            R.mipmap.badge_photographer_100_foreground,
            R.mipmap.badge_photographer_1000_foreground,
            R.mipmap.badge_photographer_10000_foreground
        )
        badgesGreenThumb = listOf(
            R.mipmap.badge_greenthumb_1_foreground,
            R.mipmap.badge_greenthumb_10_foreground,
            R.mipmap.badge_greenthumb_100_foreground,
            R.mipmap.badge_greenthumb_1000_foreground,
            R.mipmap.badge_greenthumb_10000_foreground
        )
        badgesOfBadges = listOf(
            R.mipmap.badge_ofbadges_5_foreground,
            R.mipmap.badge_ofbadges_10_foreground,
            R.mipmap.badge_ofbadges_20_foreground
        )
    }
}