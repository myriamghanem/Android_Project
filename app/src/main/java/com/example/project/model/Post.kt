package com.example.project.model

public data class Post(
    var id: String = "",
    val itemName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val userId: Int?  = null,
    var Isfound: Boolean= false,

)
