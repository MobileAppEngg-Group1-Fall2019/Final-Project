package com.group_01.finalproject.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.group_01.finalproject.R
import com.group_01.finalproject.db.CareModel
import com.group_01.finalproject.db.DBInterface
import com.group_01.finalproject.db.UserModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.history_card.view.*
import java.util.*
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.log2

class HomeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dbHelper = DBInterface(context = this.context!!)

        // Updates user data with current db information for points & badges.
        updateUser()

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
        val mDate = formatDate(mUser?.creationDate) // Date format: M/D/Y

        value_total_points.text = "Total Points: $points"
        value_start_date.text = "Gardening Since ${mDate[0]} ${mDate[1]}, ${mDate[2]}!"
    }

    // Create and add image view of badges to badges_layout using User db data.
    private fun createBadges() {
        for(i in 0 until mUser!!.consistencyBadge) {  // CONSISTENCY BADGE  // Range: 0-5
            addBadge(badgesConsistency, i)
        }
        for(i in 0 until mUser!!.diversityBadge) {  // DIVERSITY BADGE   // Range: 0-6
            addBadge(badgesDiversity, i)
        }
        for(i in 0 until mUser!!.photosBadge) {  // PHOTOGRAPHER BADGE   // Range: 0-5
            addBadge(badgesPhotos, i)
        }
        for(i in 0 until mUser!!.greenThumbBadge) {  // GREEN THUMB BADGE   // Range: 0-5
            addBadge(badgesGreenThumb, i)
        }
        for(i in 0 until mUser!!.badgeOfBadges) { // BADGE OF BADGES   // Range: 0-3
            addBadge(badgesOfBadges, i)
        }
    }

    // Create and add cards to display care history.
    @SuppressLint("SetTextI18n")
    private fun createHistory() {
        mCare?.forEach {
            val layoutInflater = LayoutInflater.from(context)
            val plant = dbHelper.getPlant(it.plantID)
            val mDate = formatDate(it.date) // Date format: M/D/Y

            val card: View = layoutInflater.inflate( // Inflate History Card Layout.
                R.layout.history_card,
                history_cards_layout,
                false
            )

            // Set TextViews for History Card.
            card.card_name.text = "Name: ${plant.name}"
            card.card_type.text = "Type: ${plant.type}"
            card.card_date.text = "${mDate[0]} ${mDate[1]}, ${mDate[2]}"

            // This is ugly but deal with it! No ternary operators :(
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
        // To edit title and description strings, go to: 'res/values/strings.xml'
        when(type) {
            badgesConsistency -> {
                titleString = resources.getStringArray(R.array.badge_consistency_title)[index]
                descString = resources.getStringArray(R.array.badge_consistency_desc)[index]
            }
            badgesDiversity -> {
                titleString = resources.getStringArray(R.array.badge_diversity_title)[index]
                descString = resources.getStringArray(R.array.badge_diversity_desc)[index]
            }
            badgesPhotos -> {
                titleString = resources.getStringArray(R.array.badge_photographer_title)[index]
                descString = resources.getStringArray(R.array.badge_photographer_desc)[index]
            }
            badgesGreenThumb -> {
                titleString = resources.getStringArray(R.array.badge_greenThumb_title)[index]
                descString = resources.getStringArray(R.array.badge_greenThumb_desc)[index]
            }
            badgesOfBadges -> {
                titleString = resources.getStringArray(R.array.badge_ofBadges_title)[index]
                descString = resources.getStringArray(R.array.badge_ofBadges_desc)[index]
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

    // Calculates the current number of points and badges and update the User in database.
    private fun updateUser() {
        val curUser = dbHelper.getUser(1)

        // Calculates Times Watered
        var numOfWatering = 0
        dbHelper.getAllCare().forEach {
            if(it.completed)
                numOfWatering++
        }

        // Calculates Consistency Badge
        var consistAmt = 0.0
        val reverseListOfCares = dbHelper.getAllCare()
        reverseListOfCares.reverse() // Reverses the list
        run loop@{
            reverseListOfCares.forEach{
                if (!it.completed)
                    return@loop // Breaks out of forEach loop if watering streak ended.
                consistAmt++
            }
        }
        var consistency = if(consistAmt != 0.0) floor(log10(consistAmt)).toInt()+1 else 0
        // Prevent User from losing their consistency badges when their streak has ended.
        consistency = if(curUser.consistencyBadge <= consistency) consistency else curUser.consistencyBadge

        // Calculates Diversity Badge
        val diversity = dbHelper.getAllPlants().distinctBy { it.type }.size / 2

        // Calculates Photographer Badge
        val photoAmt = dbHelper.getAllImages().size.toDouble()
        val photographer = if(photoAmt != 0.0) floor(log10(photoAmt)).toInt()+1 else 0

        // Calculates Green Thumb Badge
        val greenAmt = numOfWatering.toDouble()
        val greenThumb = if(greenAmt != 0.0) floor(log10(greenAmt)).toInt()+1 else 0

        // Calculates Badge of Badges
        val total = consistency + diversity + photographer + greenThumb
        val ofBadges = if(total >= 5) floor(log2(total / 5.0)).toInt()+1 else 0

        // Calculates Points
        val points = (numOfWatering * 123) + (total * 1234)

        val newUser = UserModel(
            curUser.userId,
            curUser.name,

            // New data to be updated.
            points,
            consistency,
            diversity,
            photographer,
            greenThumb,
            ofBadges,

            curUser.creationDate,
            curUser.lat,
            curUser.long
        )

        // Informs the User with a pop-up if they have earned new badge(s).
        if (compareValuesBy(curUser, newUser,
                { it.consistencyBadge }, { it.diversityBadge }, {it.photosBadge},
                { it.greenThumbBadge }, { it.badgeOfBadges }) != 0)
        {
            val alert: AlertDialog.Builder = AlertDialog.Builder(context)
            alert.setTitle("Congratulations!") // Alert type pop-up.
                .setMessage("You have unlocked new badge(s)! Check your collection to see what you have earned.")
                .setPositiveButton("Thanks") { _, _ ->
                }.show()
        }

        // Update User in database.
        dbHelper.updateUser(newUser)
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

    // Formats Date constructor format to MONTH - DAY - YEAR format.
    private fun formatDate(date: Date?): List<Any> {
        val calendar: Calendar = GregorianCalendar()
        calendar.time = date
        val year = calendar[Calendar.YEAR]
        val month = monthToString(calendar[Calendar.MONTH])
        val day = calendar[Calendar.DAY_OF_MONTH]

        return listOf(month,day,year)
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