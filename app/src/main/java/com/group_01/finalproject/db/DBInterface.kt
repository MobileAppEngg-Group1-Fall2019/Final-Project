package com.group_01.finalproject.db

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

class DBInterface(context: Context) {
    private lateinit var dbHelper: db

    init {
        dbHelper = db(context)
    }

    fun closeConnection() {
        dbHelper.close()
    }

    fun updateCare(care: CareModel): Boolean {
        return dbHelper.updateCare(care)
    }

    fun getPlantCare(lastCareDate: Date): ArrayList<CareModel> {
        return dbHelper.readPlantCare(lastCareDate)
    }

    fun insertUser(user: UserModel): Long {
        return dbHelper.insertUser(user)
    }

    fun insertPlant(plant: PlantModel): Long {
        return dbHelper.insertPlant(plant)
    }

    fun updatePlant(plant: PlantModel):  Boolean {
        return dbHelper.updatePlant(plant)
    }

    fun insertImage(image: ImageModel): Long {
        return dbHelper.insertImage(image)
    }

    fun insertCare(care: CareModel): Long {
        return dbHelper.insertCare(care)
    }

    fun getUser(userID: Long): UserModel {
        return dbHelper.readUser(userID).get(0)
    }

    fun updateUser(user: UserModel): Boolean {
        return dbHelper.updateUser(user)
    }

    fun getPlant(plantID: Long): PlantModel {
        return dbHelper.readPlant(plantID).get(0)
    }

    fun getImage(imageID: Long): ImageModel {
        return dbHelper.readImage(imageID).get(0)
    }

    fun getPlantImages(plantID: Long): ArrayList<ImageModel> {
        return dbHelper.readPlantImages(plantID)
    }

    fun getCare(careID: Long): CareModel {
        return dbHelper.readCare(careID).get(0)
    }

    fun getAllUsers(): ArrayList<UserModel> {
        return dbHelper.readAllUsers()
    }

    fun getAllCare(): ArrayList<CareModel> {
        return dbHelper.readAllCare()
    }

    fun getAllPlants(): ArrayList<PlantModel> {
        return dbHelper.readAllPlants()
    }

    fun getAllImages(): ArrayList<ImageModel> {
        return dbHelper.readAllImages()
    }
}