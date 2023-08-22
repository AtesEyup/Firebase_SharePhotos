package com.eypates.firebase.model

import com.google.firebase.Timestamp

data class DataModel(
    val url: String = "",
    val email: String = "",
    val comment: String = "",
    val dateTime: Timestamp,
)
