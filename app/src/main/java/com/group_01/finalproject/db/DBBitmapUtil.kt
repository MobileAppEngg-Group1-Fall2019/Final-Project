package com.group_01.finalproject.db

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.ByteArrayOutputStream
import java.net.URL


object DbBitmapUtil {

    fun imageURLToBitmap(location: String): Bitmap {
        var url: URL = URL(location)
        return BitmapFactory.decodeStream(url.openStream());
    }

    fun imageFileToBitmap(location: String): Bitmap {
        return BitmapFactory.decodeFile(location)
    }

    // convert from bitmap to byte array
    fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    // convert from byte array to bitmap
    fun getImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}