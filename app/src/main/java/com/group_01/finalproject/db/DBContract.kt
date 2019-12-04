package com.group_01.finalproject.db

import android.provider.BaseColumns

object DBContract {
    class UserEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val USER_ID = "userid"
            const val NAME = "name"
            const val POINTS = "points"
            const val CONSISTENCY_BADGE = "consistencybadge"
            const val DIVERSITY_BADGE = "diversitybadge"
            const val PHOTOS_BADGE = "photosbadge"
            const val GREEN_THUMB_BADGE = "greenthumbbadge"
            const val BADGE_OF_BADGES = "badgeofbadges"
        }
    }

    class PlantEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "plant"
            const val PLANT_ID = "plantid"
            const val NAME = "name"
            const val TYPE = "type"
            const val STATUS = "status"
            const val INDOOR = "indoor"
            const val AGE = "age"
        }
    }

    class CareEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "care"
            const val CARE_ID = "careid"
            const val PLANT_ID = "plantid"
            const val DATE = "date"
            const val CAPTION = "caption"
        }
    }

    class ImageEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "image"
            const val IMAGE_ID = "imageid"
            const val PLANT_ID = "plantid"
            const val LOCATION = "location"
            const val LAST_MODIFIED = "lastmodified"
        }
    }
}