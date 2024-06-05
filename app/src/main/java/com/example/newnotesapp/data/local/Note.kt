package com.example.newnotesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val isLocked: Boolean,
    @PrimaryKey
    val id: Int? = null
)
