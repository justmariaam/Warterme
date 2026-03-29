package com.example.waterme.data

import com.example.waterme.model.Plant
import java.util.concurrent.TimeUnit

interface WaterRepository {
    fun scheduleReminder(duration: Long, unit: TimeUnit, plantName: String)
    val plants: List<Plant>
}
