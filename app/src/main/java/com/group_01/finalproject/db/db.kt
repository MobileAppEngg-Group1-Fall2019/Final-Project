package com.group_01.finalproject.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.PointF
import android.util.Log
import androidx.annotation.IntegerRes
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class db(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    var dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER)
        db.execSQL(SQL_CREATE_PLANT)
        db.execSQL(SQL_CREATE_IMAGE)
        db.execSQL(SQL_CREATE_CARE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_USER)
        db.execSQL(SQL_DELETE_PLANT)
        db.execSQL(SQL_DELETE_IMAGE)
        db.execSQL(SQL_DELETE_CARE)
        onCreate(db)

    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(user: UserModel): Long {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.UserEntry.NAME, user.name)
        values.put(DBContract.UserEntry.POINTS, user.points)
        values.put(DBContract.UserEntry.CONSISTENCY_BADGE, user.consistencyBadge)
        values.put(DBContract.UserEntry.DIVERSITY_BADGE, user.diversityBadge)
        values.put(DBContract.UserEntry.PHOTOS_BADGE, user.photosBadge)
        values.put(DBContract.UserEntry.GREEN_THUMB_BADGE, user.greenThumbBadge)
        values.put(DBContract.UserEntry.BADGE_OF_BADGES, user.badgeOfBadges)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME, null, values)

        return newRowId
    }

    fun readUser(userId: Long): ArrayList<UserModel> {
        val users = ArrayList<UserModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.UserEntry.TABLE_NAME} WHERE ${DBContract.UserEntry.USER_ID} = ${userId}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_USER)
            return ArrayList()
        }
        var userId: Long
        var name: String
        var points: Int
        var consistencyBadge: Int
        var diversityBadge: Int
        var photosBadge: Int
        var greenThumbBadge: Int
        var badgeOfBadges: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                userId = cursor.getLong(cursor.getColumnIndex(DBContract.UserEntry.USER_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.NAME))
                points = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.POINTS))
                diversityBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.DIVERSITY_BADGE))
                consistencyBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.CONSISTENCY_BADGE))
                photosBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.PHOTOS_BADGE))
                greenThumbBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.GREEN_THUMB_BADGE));
                badgeOfBadges =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.BADGE_OF_BADGES));
                users.add(
                    UserModel(
                        userId,
                        name,
                        points,
                        consistencyBadge,
                        diversityBadge,
                        photosBadge,
                        greenThumbBadge,
                        badgeOfBadges
                    )
                )
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readAllUsers(): ArrayList<UserModel> {
        val checkIns = ArrayList<UserModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query = "SELECT * FROM ${DBContract.UserEntry.TABLE_NAME}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_USER)
            return ArrayList()
        }
        var userId: Long
        var name: String
        var points: Int
        var consistencyBadge: Int
        var diversityBadge: Int
        var photosBadge: Int
        var greenThumbBadge: Int
        var badgeOfBadges: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                userId = cursor.getLong(cursor.getColumnIndex(DBContract.UserEntry.USER_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.NAME))
                points = cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.POINTS))
                diversityBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.DIVERSITY_BADGE))
                consistencyBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.CONSISTENCY_BADGE))
                photosBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.PHOTOS_BADGE))
                greenThumbBadge =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.GREEN_THUMB_BADGE));
                badgeOfBadges =
                    cursor.getInt(cursor.getColumnIndex(DBContract.UserEntry.BADGE_OF_BADGES));
                checkIns.add(
                    UserModel(
                        userId,
                        name,
                        points,
                        consistencyBadge,
                        diversityBadge,
                        photosBadge,
                        greenThumbBadge,
                        badgeOfBadges
                    )
                )
                cursor.moveToNext()
            }
        }
        return checkIns
    }

    @Throws(SQLiteConstraintException::class)
    fun insertPlant(plant: PlantModel): Long {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.PlantEntry.NAME, plant.name)
        values.put(DBContract.PlantEntry.TYPE, plant.type)
        values.put(DBContract.PlantEntry.STATUS, plant.status)
        values.put(DBContract.PlantEntry.INDOOR, plant.indoor)
        values.put(DBContract.PlantEntry.AGE, plant.age)


        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.PlantEntry.TABLE_NAME, null, values)

        return newRowId
    }

    fun readPlant(plantId: Long): ArrayList<PlantModel> {
        val plants = ArrayList<PlantModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.PlantEntry.TABLE_NAME} WHERE ${DBContract.PlantEntry.PLANT_ID} = ${plantId}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_PLANT)
            return ArrayList()
        }
        var plantId: Long
        var name: String
        var type: String
        var status: String
        var indoor: Boolean
        var age: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.PlantEntry.PLANT_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.PlantEntry.NAME))
                type = cursor.getString(cursor.getColumnIndex(DBContract.PlantEntry.TYPE))
                status = cursor.getString(cursor.getColumnIndex(DBContract.PlantEntry.STATUS))
                indoor = cursor.getInt(cursor.getColumnIndex(DBContract.PlantEntry.INDOOR)) > 0
                age = cursor.getInt(cursor.getColumnIndex(DBContract.PlantEntry.AGE))
                plants.add(PlantModel(plantId, name, type, status, indoor, age))
                cursor.moveToNext()
            }
        }
        return plants
    }

    fun readAllPlants(): ArrayList<PlantModel> {
        val plants = ArrayList<PlantModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query = "SELECT * FROM ${DBContract.PlantEntry.TABLE_NAME}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_PLANT)
            return ArrayList()
        }
        var plantId: Long
        var name: String
        var type: String
        var status: String
        var indoor: Boolean
        var age: Int

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.PlantEntry.PLANT_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.PlantEntry.NAME))
                type = cursor.getString(cursor.getColumnIndex(DBContract.PlantEntry.TYPE))
                status = cursor.getString(cursor.getColumnIndex(DBContract.PlantEntry.STATUS))
                indoor = cursor.getInt(cursor.getColumnIndex(DBContract.PlantEntry.INDOOR)) > 0
                age = cursor.getInt(cursor.getColumnIndex(DBContract.PlantEntry.AGE))
                plants.add(PlantModel(plantId, name, type, status, indoor, age))
                cursor.moveToNext()
            }
        }
        return plants
    }

    fun insertImage(image: ImageModel): Long {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.ImageEntry.PLANT_ID, image.plantID)
        values.put(DBContract.ImageEntry.LOCATION, image.location)
        values.put(DBContract.ImageEntry.LAST_MODIFIED, dateFormatter.format(image.lastModified))


        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.ImageEntry.TABLE_NAME, null, values)

        return newRowId
    }

    fun readImage(imageId: Long): ArrayList<ImageModel> {
        val plants = ArrayList<ImageModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.ImageEntry.TABLE_NAME} WHERE ${DBContract.ImageEntry.IMAGE_ID} = ${imageId}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_IMAGE)
            return ArrayList()
        }
        var imageId: Long
        var location: String
        var lastModified: String
        var plantId: Long

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                imageId = cursor.getLong(cursor.getColumnIndex(DBContract.ImageEntry.IMAGE_ID))
                location = cursor.getString(cursor.getColumnIndex(DBContract.ImageEntry.LOCATION))
                lastModified = cursor.getString(cursor.getColumnIndex(DBContract.ImageEntry.LAST_MODIFIED))
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.ImageEntry.PLANT_ID))
                plants.add(ImageModel(imageId,plantId,location, dateFormatter.parse(lastModified)))
                cursor.moveToNext()
            }
        }
        return plants
    }

    fun readPlantImages(plantId: Long): ArrayList<ImageModel> {
        val plants = ArrayList<ImageModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.ImageEntry.TABLE_NAME} WHERE ${DBContract.ImageEntry.PLANT_ID} = ${plantId}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_IMAGE)
            return ArrayList()
        }
        var imageId: Long
        var location: String
        var lastModified: String
        var plantId: Long

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                imageId = cursor.getLong(cursor.getColumnIndex(DBContract.ImageEntry.IMAGE_ID))
                location = cursor.getString(cursor.getColumnIndex(DBContract.ImageEntry.LOCATION))
                lastModified = cursor.getString(cursor.getColumnIndex(DBContract.ImageEntry.LAST_MODIFIED))
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.ImageEntry.PLANT_ID))
                plants.add(ImageModel(imageId,plantId,location, dateFormatter.parse(lastModified)))
                cursor.moveToNext()
            }
        }
        return plants
    }

    fun insertCare(care: CareModel): Long {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()

        values.put(DBContract.CareEntry.DATE, dateFormatter.format(care.date))
        values.put(DBContract.CareEntry.CAPTION, care.caption)
        values.put(DBContract.CareEntry.PLANT_ID, care.plantID)


        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.CareEntry.TABLE_NAME, null, values)

        return newRowId
    }

    fun readCare(careId: Long): ArrayList<CareModel> {
        val cares = ArrayList<CareModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.CareEntry.TABLE_NAME} WHERE ${DBContract.CareEntry.CARE_ID} = ${careId}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_IMAGE)
            return ArrayList()
        }
        var careId: Long
        var date: String
        var caption: String
        var plantId: Long

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                careId = cursor.getLong(cursor.getColumnIndex(DBContract.CareEntry.CARE_ID))
                date = cursor.getString(cursor.getColumnIndex(DBContract.CareEntry.DATE))
                caption = cursor.getString(cursor.getColumnIndex(DBContract.CareEntry.CAPTION))
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.CareEntry.PLANT_ID))
                cares.add(CareModel(careId, plantId, dateFormatter.parse(date), caption))
                cursor.moveToNext()
            }
        }
        return cares
    }

    fun readAllCare(careId: Long): ArrayList<CareModel> {
        val cares = ArrayList<CareModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.CareEntry.TABLE_NAME}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_IMAGE)
            return ArrayList()
        }
        var careId: Long
        var date: String
        var caption: String
        var plantId: Long

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                careId = cursor.getLong(cursor.getColumnIndex(DBContract.CareEntry.CARE_ID))
                date = cursor.getString(cursor.getColumnIndex(DBContract.CareEntry.DATE))
                caption = cursor.getString(cursor.getColumnIndex(DBContract.CareEntry.CAPTION))
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.CareEntry.PLANT_ID))
                cares.add(CareModel(careId, plantId, dateFormatter.parse(date), caption))
                cursor.moveToNext()
            }
        }
        return cares
    }

    fun readPlantCares(plantId: Long): ArrayList<CareModel> {
        val cares = ArrayList<CareModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        val query =
            "SELECT * FROM ${DBContract.CareEntry.TABLE_NAME} WHERE ${DBContract.CareEntry.PLANT_ID} = ${plantId}"

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_IMAGE)
            return ArrayList()
        }
        var careId: Long
        var date: String
        var caption: String
        var plantId: Long

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                careId = cursor.getLong(cursor.getColumnIndex(DBContract.CareEntry.CARE_ID))
                date = cursor.getString(cursor.getColumnIndex(DBContract.CareEntry.DATE))
                caption = cursor.getString(cursor.getColumnIndex(DBContract.CareEntry.CAPTION))
                plantId = cursor.getLong(cursor.getColumnIndex(DBContract.CareEntry.PLANT_ID))
                cares.add(CareModel(careId, plantId, dateFormatter.parse(date), caption))
                cursor.moveToNext()
            }
        }
        return cares
    }


    /**
     * Calculates the end-point from a given source at a given range (meters)
     * and bearing (degrees). This methods uses simple geometry equations to
     * calculate the end-point.
     *
     * @param point
     * Point of origin
     * @param range
     * Range in meters
     * @param bearing
     * Bearing in degrees
     * @return End-point from the source given the desired range and bearing.

    fun calculateDerivedPosition(
        point: PointF,
        range: Double, bearing: Double
    ): PointF {
        val EarthRadius = 6371000.0 // m

        val latA = Math.toRadians(point.x.toDouble())
        val lonA = Math.toRadians(point.y.toDouble())
        val angularDistance = range / EarthRadius
        val trueCourse = Math.toRadians(bearing)

        var lat = Math.asin(
            Math.sin(latA) * Math.cos(angularDistance) + (Math.cos(latA) * Math.sin(angularDistance)
                    * Math.cos(trueCourse))
        )

        val dlon = Math.atan2(
            (Math.sin(trueCourse) * Math.sin(angularDistance)
                    * Math.cos(latA)),
            Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat)
        )

        var lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI

        lat = Math.toDegrees(lat)
        lon = Math.toDegrees(lon)
        return PointF(lat.toFloat(), lon.toFloat())

    }

    fun checkForCloseCheckpoint(latitude: Double, longitude: Double): ArrayList<CheckInModel> {
        val checkIns = ArrayList<CheckInModel>()
        val db = writableDatabase
        var cursor: Cursor? = null

        val center = PointF(latitude.toFloat(), longitude.toFloat())
        val mult = 1.0 // mult = 1.1; is more reliable
        val p1 = calculateDerivedPosition(center, mult * 30, 0.toDouble())
        val p2 = calculateDerivedPosition(center, mult * 30, 90.toDouble())
        val p3 = calculateDerivedPosition(center, mult * 30, 180.toDouble())
        val p4 = calculateDerivedPosition(center, mult * 30, 270.toDouble())
        var join =
            "${DBContract.LocationEntry.TABLE_NAME} INNER JOIN ${DBContract.CheckInEntry.TABLE_NAME} ON location.locationid = checkin.locationid"
        var select =
            "SELECT ${DBContract.LocationEntry.TABLE_NAME}.${DBContract.LocationEntry.COLUMN_LOCATION_ID} AS ${DBContract.JoinEntry.COLUMN_LOCATION_ID}, ${DBContract.LocationEntry.COLUMN_NAME}, ${DBContract.LocationEntry.COLUMN_ADDRESS}, ${DBContract.CheckInEntry.COLUMN_CHECKIN_ID}, ${DBContract.CheckInEntry.COLUMN_LATITUDE}, ${DBContract.CheckInEntry.COLUMN_LONGITUDE}, ${DBContract.CheckInEntry.COLUMN_TIME}, ${DBContract.CheckInEntry.COLUMN_CENTER}"
        var whereQuery =
            " WHERE " + DBContract.CheckInEntry.COLUMN_LATITUDE + " >= " + p3.x.toString() + " AND " + DBContract.CheckInEntry.COLUMN_LATITUDE + " <= " + p1.x.toString() + " AND " + DBContract.CheckInEntry.COLUMN_LONGITUDE + " <= " + p2.y.toString() + " AND " + DBContract.CheckInEntry.COLUMN_LONGITUDE + " >= " + p4.y.toString() + " AND " + DBContract.CheckInEntry.COLUMN_CENTER + " = 1"
        val query = "${select} FROM ${join}\n" + whereQuery
        Log.d("### Executing query", query)
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_LOCATION)
            return ArrayList()
        }

        var checkInId: Long
        var locationid: Long
        var name: String
        var latitude: Double
        var longitude: Double
        var address: String
        var time: String
        var centerCheckIn: Boolean
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                checkInId =
                    cursor.getLong(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_CHECKIN_ID))
                locationid =
                    cursor.getLong(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_LOCATION_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_NAME))
                latitude =
                    cursor.getDouble(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_LATITUDE))
                longitude =
                    cursor.getDouble(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_LONGITUDE))
                address =
                    cursor.getString(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_ADDRESS))
                time = cursor.getString(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_TIME))
                centerCheckIn =
                    cursor.getInt(cursor.getColumnIndex(DBContract.JoinEntry.COLUMN_CENTER)) > 0;
                checkIns.add(
                    CheckInModel(
                        checkInId,
                        locationid,
                        name,
                        latitude,
                        longitude,
                        address,
                        time,
                        centerCheckIn
                    )
                )
                cursor.moveToNext()
            }
        }
        return checkIns
    }


    fun readAllLocations(): ArrayList<LocationModel> {
        val locations = ArrayList<LocationModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.LocationEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_LOCATION)
            return ArrayList()
        }

        var locationid: Long
        var name: String
        var latitude: Double
        var longitude: Double
        var address: String
        var time: String
        var currentDateTime: String = DateFormat.getDateTimeInstance().format(Date())
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                locationid =
                    cursor.getLong(cursor.getColumnIndex(DBContract.LocationEntry.COLUMN_LOCATION_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.LocationEntry.COLUMN_NAME))
                address =
                    cursor.getString(cursor.getColumnIndex(DBContract.LocationEntry.COLUMN_ADDRESS))
                locations.add(LocationModel(locationid, name, address))
                cursor.moveToNext()
            }
        }
        return locations
    }
     */
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Plants.db"

        private const val SQL_CREATE_USER =
            "CREATE TABLE ${DBContract.UserEntry.TABLE_NAME} (" +
                    "${DBContract.UserEntry.USER_ID} INTEGER PRIMARY KEY," +
                    "${DBContract.UserEntry.NAME} TEXT," +
                    "${DBContract.UserEntry.POINTS} INTEGER," +
                    "${DBContract.UserEntry.CONSISTENCY_BADGE} INTEGER," +
                    "${DBContract.UserEntry.DIVERSITY_BADGE} INTEGER," +
                    "${DBContract.UserEntry.PHOTOS_BADGE} INTEGER," +
                    "${DBContract.UserEntry.GREEN_THUMB_BADGE} INTEGER," +
                    "${DBContract.UserEntry.BADGE_OF_BADGES})"


        private const val SQL_DELETE_USER =
            "DROP TABLE IF EXISTS ${DBContract.UserEntry.TABLE_NAME}"

        private const val SQL_CREATE_PLANT = "CREATE TABLE ${DBContract.PlantEntry.TABLE_NAME} (" +
                "${DBContract.PlantEntry.PLANT_ID} INTEGER PRIMARY KEY," +
                "${DBContract.PlantEntry.NAME} TEXT," +
                "${DBContract.PlantEntry.TYPE} TEXT," +
                "${DBContract.PlantEntry.STATUS} TEXT," +
                "${DBContract.PlantEntry.INDOOR} INTEGER," +
                "${DBContract.PlantEntry.AGE} INTEGER)"

        private const val SQL_DELETE_PLANT =
            "DROP TABLE IF EXISTS ${DBContract.PlantEntry.TABLE_NAME}"


        private const val SQL_CREATE_IMAGE = "CREATE TABLE ${DBContract.ImageEntry.TABLE_NAME} (" +
                "${DBContract.ImageEntry.IMAGE_ID} INTEGER PRIMARY KEY," +
                "${DBContract.ImageEntry.LOCATION} TEXT," +
                "${DBContract.ImageEntry.LAST_MODIFIED} TEXT," +
                "${DBContract.ImageEntry.PLANT_ID} INTEGER," +
                "FOREIGN KEY(${DBContract.ImageEntry.PLANT_ID}) REFERENCES ${DBContract.PlantEntry.TABLE_NAME}(${DBContract.PlantEntry.PLANT_ID}) ON DELETE CASCADE)"

        private const val SQL_DELETE_IMAGE =
            "DROP TABLE IF EXISTS ${DBContract.PlantEntry.TABLE_NAME}"

        private const val SQL_CREATE_CARE = "CREATE TABLE ${DBContract.CareEntry.TABLE_NAME} (" +
                "${DBContract.CareEntry.CARE_ID} INTEGER PRIMARY KEY," +
                "${DBContract.CareEntry.DATE} TEXT," +
                "${DBContract.CareEntry.CAPTION} TEXT," +
                "${DBContract.CareEntry.PLANT_ID} INTEGER" +
                "FOREIGN KEY(${DBContract.CareEntry.PLANT_ID} REFERENCES ${DBContract.PlantEntry.TABLE_NAME}(${DBContract.PlantEntry.PLANT_ID}) ON DELETE CASCADE)"

        private const val SQL_DELETE_CARE =
            "DROP TABLE IF EXISTS ${DBContract.CareEntry.TABLE_NAME}"
    }
}

