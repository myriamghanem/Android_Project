package com.example.project.model

public data class Found(
    var id: String = "",
    val itemName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val userId: String? = null,
    val Isclaimed: Boolean= false
)