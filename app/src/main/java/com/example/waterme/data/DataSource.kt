package com.example.waterme.data

import com.example.waterme.R
import com.example.waterme.model.Plant

object DataSource {
    val plants = listOf(
        Plant(
            name = R.string.lithop,
            schedule = R.string.monthly,
            type = R.string.succulent,
            description = R.string.stone_mimicking_succulent
        ),
        Plant(
            name = R.string.carrot,
            schedule = R.string.daily,
            type = R.string.root,
            description = R.string.hardy_root_vegetable
        ),
        Plant(
            name = R.string.peony,
            schedule = R.string.weekly,
            type = R.string.flower,
            description = R.string.spring_blooming_flower
        ),
        Plant(
            name = R.string.pothos,
            schedule = R.string.weekly,
            type = R.string.houseplant,
            description = R.string.indoor_vine
        ),
        Plant(
            name = R.string.fiddle_leaf_fig,
            schedule = R.string.weekly,
            type = R.string.broadleaf_evergreen,
            description = R.string.ornamental_fig
        ),
        Plant(
            name = R.string.strawberry,
            schedule = R.string.daily,
            type = R.string.fruit,
            description = R.string.delicious_multiple_fruit
        )
    )
}
