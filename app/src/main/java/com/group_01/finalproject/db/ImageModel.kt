package com.group_01.finalproject.db

import java.util.*

class ImageModel(
    val imageID: Long,
    val plantID: Long,
    val data: ByteArray,
    val lastModified: Date

)