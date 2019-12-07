package com.group_01.finalproject.db

import java.util.*

class PlantModel(
    val plantId: Long,
    val name: String,
    val type: String,
    val status: String,
    val indoor: Boolean,
    val age: Int,
    val lastCare: Date
)