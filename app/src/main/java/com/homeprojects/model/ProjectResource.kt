package com.homeprojects.model

import java.util.*

data class ProjectResource (
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var purchased: Boolean
)