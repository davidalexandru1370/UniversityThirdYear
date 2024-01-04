package com.example.myapplication.domain

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Assignment(
    val id: Int,
    val description: String,
    val deadline: Instant,
    val received: Instant,
    val subject: String,
    val isDelivered: Boolean
)