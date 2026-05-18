package com.yuval.remepy_test.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val body: String,
    val isDone: Boolean,
    val creationDate: LocalDateTime,
    val dueDate: LocalDateTime
)
