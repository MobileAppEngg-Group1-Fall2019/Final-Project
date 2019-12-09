package com.group_01.finalproject.plants

object Plants {
    class Tomato {
        companion object {
            const val type = "tomato"
            const val waterFreq = 2
            const val instructions = ""
        }

    }

    class Cactus {
        companion object {
            const val waterFreq = 5
            const val instructions = ""
        }
    }

    class Peppers {
        companion object {
            const val waterFreq = 4
            const val instructions = "Try to provide one inch of water every water cycle"
        }
    }

    fun getNotificationInstructions(type: String) {

    }
}