package com.group_01.finalproject.openweather

import android.system.Os.close
import java.io.ByteArrayOutputStream
import java.net.URL
import java.net.HttpURLConnection
import java.io.InputStream
import java.io.InputStreamReader
import java.io.BufferedReader


class WeatherHttpClient {
    fun getWeatherData(location: String): String? {
        var con: HttpURLConnection? = null
        var `is`: InputStream? = null

        try {
            con = URL("$BASE_URL$location&APPID=$APPID").openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.doInput = true
            con.doOutput = true
            con.connect()

            // Let's read the response
            val buffer = StringBuffer()
            `is` = con.inputStream
            val br = BufferedReader(InputStreamReader(`is`))
            var line: String? = null
            line = br.readLine()
            while (line != null) {
                buffer.append(line!! + "rn")
                line = br.readLine()
            }
            `is`!!.close()
            con.disconnect()
            return buffer.toString()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (t: Throwable) {
            }

            try {
                con!!.disconnect()
            } catch (t: Throwable) {
            }

        }

        return null

    }

    fun getImage(code: String): ByteArray? {
        var con: HttpURLConnection? = null
        var `is`: InputStream? = null
        try {
            con = URL(IMG_URL + code).openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.doInput = true
            con.doOutput = true
            con.connect()

            // Let's read the response
            `is` = con.inputStream
            val buffer = ByteArray(1024)
            val baos = ByteArrayOutputStream()

            while (`is`!!.read(buffer) != -1)
                baos.write(buffer)

            return baos.toByteArray()
        } catch (t: Throwable) {
            t.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (t: Throwable) {
            }

            try {
                con!!.disconnect()
            } catch (t: Throwable) {
            }

        }

        return null

    }

    companion object {

        private val BASE_URL = "http://api.openweathermap.org/data/2.5/hourly?q="
        private val IMG_URL = "http://openweathermap.org/img/w/"
        private val APPID = "956fc90708ef1ae3e63d2495096ceef9"
    }
}