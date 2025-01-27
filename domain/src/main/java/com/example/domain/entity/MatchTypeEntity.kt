package com.example.domain.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MatchTypeEntity(
    @SerializedName("matchtype")
    val matchType: Int,
    val desc: String
)
