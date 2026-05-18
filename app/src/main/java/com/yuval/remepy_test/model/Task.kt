package com.yuval.remepy_test.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDateTime

class Task(
    title: String,
    body: String,
    isDone: Boolean,
    val creationDate: LocalDateTime,
    dueDate: LocalDateTime
){
    val title by mutableStateOf(title)
    val body by mutableStateOf(body)
    val isDone by mutableStateOf(isDone)
    val dueDate by mutableStateOf(dueDate)
}
