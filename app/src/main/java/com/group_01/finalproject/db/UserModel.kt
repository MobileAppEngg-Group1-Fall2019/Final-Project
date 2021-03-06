package com.group_01.finalproject.db

import java.util.*

class UserModel(
    val userId: Long,
    val name: String,
    val points: Int,
    val consistencyBadge: Int,
    val diversityBadge: Int,
    val photosBadge: Int,
    val greenThumbBadge: Int,
    val badgeOfBadges: Int,
    val creationDate: Date,
    val lat: Double,
    val long: Double
)