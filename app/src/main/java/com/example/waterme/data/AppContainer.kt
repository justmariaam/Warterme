package com.example.waterme.data

import android.content.Context

interface AppContainer {
    val waterRepository : WaterRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    override val waterRepository = WorkManagerWaterRepository(context)
}
