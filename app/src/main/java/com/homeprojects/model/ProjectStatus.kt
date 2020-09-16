package com.homeprojects.model

enum class ProjectStatus {
    INACTIVE,
    ACTIVE,
    COMPLETED;

    companion object {
        fun getName(status: ProjectStatus) = when(status) {
            INACTIVE -> "Future"
            ACTIVE -> "Current"
            COMPLETED -> "Completed"
        }
    }
}