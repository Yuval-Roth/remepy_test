package com.yuval.remepy_test.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDateTime

class Task(
    title: String,
    body: String,
    isDone: Boolean,
    val creationDate: LocalDateTime,
    dueDate: LocalDateTime
){
    var title by mutableStateOf(title)
    var body by mutableStateOf(body)
    var isDone by mutableStateOf(isDone)
    var dueDate by mutableStateOf(dueDate)
}
