package com.group_01.finalproject.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
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

        // Get User & Care data from database
        mUser = dbHelper.getUser(1) // Only one User per device.
        mCare = dbHelper.getAllCare()

        // Call methods to create fragment elements.
        createText(); createBadges(); createHistory()
    }

    // Set TextViews for total points & start date.
    @SuppressLint("SetTextI18n")
    private fun createText() {
        val points = mUser?.points.toString()
        val year = mUser?.creationDate?.year
        val month = mUser?.creationDate?.month
        val day = mUser?.creationDate?.day

        val stringMonth = monthToString(month)

        value_total_points.text = "Total Points: $points"
        value_start_date.text = "Gardening Since $stringMonth $day, $year!"
    }

    // Create and add image view of badges to badges_layout using User db data.
    private fun createBadges() {
        when (mUser?.consistencyBadge) { // CONSISTENCY BADGE  // Range: 0-5
            1 -> addBadge(badgesConsistency, 0)
            2 -> for (i in 0..1) { addBadge(badgesConsistency, i) }
            3 -> for (i in 0..2) { addBadge(badgesConsistency, i) }
            4 -> for (i in 0..3) { addBadge(badgesConsistency, i) }
            5 -> for (i in 0..4) { addBadge(badgesConsistency, i) }
        }
        when (mUser?.diversityBadge) { // DIVERSITY BADGE   // Range: 0-6
            1 -> addBadge(badgesDiversity, 0)
            2 -> for (i in 0..1) { addBadge(badgesDiversity, i) }
            3 -> for (i in 0..2) { addBadge(badgesDiversity, i) }
            4 -> for (i in 0..3) { addBadge(badgesDiversity, i) }
            5 -> for (i in 0..4) { addBadge(badgesDiversity, i) }
            6 -> for (i in 0..5) { addBadge(badgesDiversity, i) }
        }
        when (mUser?.photosBadge) { // PHOTOGRAPHER BADGE   // Range: 0-5
            1 -> addBadge(badgesPhotos, 0)
            2 -> for (i in 0..1) { addBadge(badgesPhotos, i) }
            3 -> for (i in 0..2) { addBadge(badgesPhotos, i) }
            4 -> for (i in 0..3) { addBadge(badgesPhotos, i) }
            5 -> for (i in 0..4) { addBadge(badgesPhotos, i) }
        }
        when (mUser?.greenThumbBadge) { // GREEN THUMB BADGE   // Range: 0-5
            1 -> addBadge(badgesGreenThumb, 0)
            2 -> for (i in 0..1) { addBadge(badgesGreenThumb, i) }
            3 -> for (i in 0..2) { addBadge(badgesGreenThumb, i) }
            4 -> for (i in 0..3) { addBadge(badgesGreenThumb, i) }
            5 -> for (i in 0..4) { addBadge(badgesGreenThumb, i) }
        }
        when (mUser?.badgeOfBadges) { // BADGE OF BADGES   // Range: 0-3
            1 -> addBadge(badgesOfBadges, 0)
            2 -> for (i in 0..1) { addBadge(badgesOfBadges, i) }
            3 -> for (i in 0..2) { addBadge(badgesOfBadges, i) }
        }
    }

    // Create and add cards to display care history.
    @SuppressLint("SetTextI18n")
    private fun createHistory() {
        mCare?.forEach {
            val layoutInflater = LayoutInflater.from(context)
            val plant = dbHelper.getPlant(it.plantID)
            val year = it.date.year
            val month = monthToString(it.date.month)
            val day = it.date.day

            val card: View = layoutInflater.inflate( // Inflate History Card Layout.
                R.layout.history_card,
                history_cards_layout,
                false
            )

            // Set TextViews for History Card.
            card.card_name.text = plant.name
            card.card_type.text = plant.type
            card.card_date.text = "$month $day, $year"

            // This is ugly but deal with it!
            card.card_bool.text = if (it.completed) "Watered: Yes" else "Watered: No"

            history_cards_layout.addView(card)
        }
    }

    // Adds Badge w/ OnClickListener for pop-up.
    private fun addBadge(type: List<Int>, index: Int) {
        val mImageView = ImageView(context)
        var titleString = ""
        var descString = ""
        mImageView.setImageResource(type[index])

        // Sets the title and description for pop-up.
        // To edit description strings, go to: 'res/values/strings.xml'
        when (type) {
            badgesConsistency -> {
                when (index) {
                    0 -> {
                        titleString = "Consistency Badge 1"
                        descString = getString(R.string.badge_consistency_1)
                    }
                    1 -> {
                        titleString = "Consistency Badge 10"
                        descString = getString(R.string.badge_consistency_10)
                    }
                    2 -> {
                        titleString = "Consistency Badge 100"
                        descString = getString(R.string.badge_consistency_100)
                    }
                    3 -> {
                        titleString = "Consistency Badge 1000"
                        descString = getString(R.string.badge_consistency_1000)
                    }
                    4 -> {
                        titleString = "Consistency Badge 10000"
                        descString = getString(R.string.badge_consistency_10000)
                    }
                }
            }
            badgesDiversity -> {
                when (index) {
                    0 -> {
                        titleString = "Diversity Badge 2"
                        descString = getString(R.string.badge_diversity_2)
                    }
                    1 -> {
                        titleString = "Diversity Badge 4"
                        descString = getString(R.string.badge_diversity_4)
                    }
                    2 -> {
                        titleString = "Diversity Badge 6"
                        descString = getString(R.string.badge_diversity_6)
                    }
                    3 -> {
                        titleString = "Diversity Badge 8"
                        descString = getString(R.string.badge_diversity_8)
                    }
                    4 -> {
                        titleString = "Diversity Badge 10"
                        descString = getString(R.string.badge_diversity_10)
                    }
                    5 -> {
                        titleString = "Diversity Badge 12"
                        descString = getString(R.string.badge_diversity_12)
                    }
                }
            }
            badgesPhotos -> {
                when (index) {
                    0 -> {
                        titleString = "Photographer Badge 1"
                        descString = getString(R.string.badge_photography_1)
                    }
                    1 -> {
                        titleString = "Photographer Badge 10"
                        descString = getString(R.string.badge_photography_10)
                    }
                    2 -> {
                        titleString = "Photographer Badge 100"
                        descString = getString(R.string.badge_photography_100)
                    }
                    3 -> {
                        titleString = "Photographer Badge 1000"
                        descString = getString(R.string.badge_photography_1000)
                    }
                    4 -> {
                        titleString = "Photographer Badge 10000"
                        descString = getString(R.string.badge_photography_10000)
                    }
                }
            }
            badgesGreenThumb -> {
                when (index) {
                    0 -> {
                        titleString = "Green Thumb Badge 1"
                        descString = getString(R.string.badge_greenThumb_1)
                    }
                    1 -> {
                        titleString = "Green Thumb Badge 10"
                        descString = getString(R.string.badge_greenThumb_10)
                    }
                    2 -> {
                        titleString = "Green Thumb Badge 100"
                        descString = getString(R.string.badge_greenThumb_100)
                    }
                    3 -> {
                        titleString = "Green Thumb Badge 1000"
                        descString = getString(R.string.badge_greenThumb_1000)
                    }
                    4 -> {
                        titleString = "Green Thumb Badge 10000"
                        descString = getString(R.string.badge_greenThumb_10000)
                    }
                }
            }
            badgesOfBadges -> {
                when (index) {
                    0 -> {
                        titleString = "Badge of Badges 5"
                        descString = getString(R.string.badge_ofBadges_5)
                    }
                    1 -> {
                        titleString = "Badge of Badges 10"
                        descString = getString(R.string.badge_ofBadges_10)
                    }
                    2 -> {
                        titleString = "Badge of Badges 20"
                        descString = getString(R.string.badge_ofBadges_20)
                    }
                }
            }
        }

        // Creates a pop-up when user presses on badge.
        mImageView.setOnClickListener {
            val alert: AlertDialog.Builder = AlertDialog.Builder(context)
            val mImageView2 = ImageView(context)
            mImageView2.setImageResource(type[index])

            alert.setTitle(titleString) // Alert type pop-up.
                .setView(mImageView2)
                .setMessage(descString)
                .setPositiveButton("Ok") { _, _ ->
                }.show()
        }

        badges_layout.addView(mImageView) // Add to badges holder view.
    }

    // Changes numerical month value from Date to equivalent string name.
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

    // Create lists containing references to badge image locations
    init {
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