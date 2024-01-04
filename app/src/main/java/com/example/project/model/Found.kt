package com.example.project.model

public data class Found(
    var id: String = "",
    val itemName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val userId: Int? = null,
    val Isclaimed: Boolean= false
)