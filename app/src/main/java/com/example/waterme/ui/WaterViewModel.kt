package com.example.waterme.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.waterme.WaterMeApplication
import com.example.waterme.data.Reminder
import com.example.waterme.data.WaterRepository

class WaterViewModel(private val waterRepository: WaterRepository) : ViewModel() {

    internal val plants = waterRepository.plants

    fun scheduleReminder(reminder: Reminder) {
        waterRepository.scheduleReminder(reminder.duration, reminder.unit, reminder.plantName)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val waterRepository =
                    (this[APPLICATION_KEY] as WaterMeApplication).container.waterRepository
                WaterViewModel(
                    waterRepository = waterRepository
                )
            }
        }
    }
}
