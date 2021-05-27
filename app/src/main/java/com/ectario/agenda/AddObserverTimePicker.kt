package com.ectario.agenda

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddObserverTimePicker : ViewModel() {
    val endTime = MutableLiveData<String>()
    val startTime = MutableLiveData<String>()
}