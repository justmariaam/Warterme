package com.example.waterme.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize


@Parcelize
data class Plant(
    @StringRes val name: Int,
    @StringRes val type: Int,
    @StringRes val description: Int,
    @StringRes val schedule: Int
): Parcelable
