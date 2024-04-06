package com.app.novatech.model

import java.util.Date

data class ProjectForList(
    val name : String,
    val responsible : String,
    val status : String,
    val startDate : Date,
    val endDate : Date
)
