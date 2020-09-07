package com.homeprojects.model

import java.util.*

data class ProjectTask (
        val id: String = UUID.randomUUID().toString(),
        var title: String,
        var description: String,
        var completed: Boolean
)