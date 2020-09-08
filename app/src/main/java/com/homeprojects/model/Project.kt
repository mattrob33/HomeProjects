package com.homeprojects.model

import java.util.*

data class Project (
    var title: String = "",
    var description: String = "",
    var status: ProjectStatus = ProjectStatus.INACTIVE,
    var priority: Priority = Priority.NONE,
    var size: ProjectSize = ProjectSize.NONE,
    var cost: Cost = Cost.FREE,
    var location: Location = Location.INTERIOR,
    var tasks: List<ProjectTask> = mutableListOf(),
    var resources: List<ProjectResource> = mutableListOf(),
    var tags: List<Tag> = mutableListOf(),
    val id: String = UUID.randomUUID().toString()
)