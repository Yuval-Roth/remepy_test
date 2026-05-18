package com.yuval.remepy_test.model

import android.content.Context
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class TaskRepository(context: Context) {
    private val taskDao = TaskDatabase.getInstance(context).taskDao()

    val tasks: Flow<List<Task>> = taskDao.observeTasks()

    suspend fun addTask(
        title: String,
        body: String,
        dueDate: LocalDateTime
    ) {
        taskDao.insert(
            Task(
                title = title,
                body = body,
                isDone = false,
                creationDate = LocalDateTime.now(),
                dueDate = dueDate
            )
        )
    }

    suspend fun updateTask(
        task: Task,
        title: String,
        body: String,
        dueDate: LocalDateTime
    ) {
        taskDao.update(
            task.copy(
                title = title,
                body = body,
                dueDate = dueDate
            )
        )
    }

    suspend fun setTaskDone(task: Task, isDone: Boolean) {
        taskDao.update(task.copy(isDone = isDone))
    }

    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }
}
