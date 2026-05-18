package com.yuval.remepy_test.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yuval.remepy_test.model.Task
import com.yuval.remepy_test.model.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TaskRepository(application)

    val tasks: StateFlow<List<Task>> = repository.tasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addTask(
        title: String,
        body: String,
        dueDate: LocalDateTime
    ) {
        viewModelScope.launch {
            repository.addTask(
                title = title,
                body = body,
                dueDate = dueDate
            )
        }
    }

    fun updateTask(
        task: Task,
        title: String,
        body: String,
        dueDate: LocalDateTime
    ) {
        viewModelScope.launch {
            repository.updateTask(
                task = task,
                title = title,
                body = body,
                dueDate = dueDate
            )
        }
    }

    fun markTaskDone(task: Task) {
        viewModelScope.launch {
            repository.setTaskDone(task, true)
        }
    }

    fun markTaskNotDone(task: Task) {
        viewModelScope.launch {
            repository.setTaskDone(task, false)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
