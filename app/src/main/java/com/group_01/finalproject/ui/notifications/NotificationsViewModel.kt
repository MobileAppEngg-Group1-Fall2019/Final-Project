package com.group_01.finalproject.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Select your plant to view your photos of them!"
    }
    val text: LiveData<String> = _text
}