package com.yuval.remepy_test.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yuval.remepy_test.model.Task
import com.yuval.remepy_test.view.components.TaskCard
import com.yuval.remepy_test.view.components.TaskInputForm
import com.yuval.remepy_test.view.components.ActionBar
import com.yuval.remepy_test.view.components.BottomSheet
import com.yuval.remepy_test.view.components.TodoHeader
import com.yuval.remepy_test.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Main()
            }
        }
    }


    @Composable
    fun Main() {
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        var taskBeingEdited by remember { mutableStateOf<Task?>(null) }
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val tasks = remember {
            mutableStateListOf(
                Task(
                    title = "Finish task card component",
                    body = "Create a polished reusable card with collapsed and expanded states, a due date section, and an action menu.",
                    isDone = false,
                    creationDate = LocalDateTime.now(),
                    dueDate = LocalDateTime.now().plusDays(2).withHour(18).withMinute(0)
                ),
                Task(
                    title = "Review reminders flow",
                    body = "Check how task actions should connect to the rest of the app once edit and delete flows exist.",
                    isDone = false,
                    creationDate = LocalDateTime.now(),
                    dueDate = LocalDateTime.now().plusDays(5).withHour(10).withMinute(30)
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                ,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TodoHeader()
                ActionBar(
                    onAddTaskClick = {
                        taskBeingEdited = null
                        showBottomSheet = true
                    }
                )
                tasks.forEach { task ->
                    TaskCard(
                        task = task,
                        onEdit = { selectedTask ->
                            taskBeingEdited = selectedTask
                            showBottomSheet = true
                        }
                    )
                }
            }
        }

        if(showBottomSheet){
            val onDismiss = {
                showBottomSheet = false
                taskBeingEdited = null
            }
            BottomSheet(
                bottomSheetState,
                onDismiss = onDismiss
            ) {
                TaskInputForm(
                    task = taskBeingEdited,
                    onSave = { input ->
                        val editedTask = taskBeingEdited
                        if (editedTask == null) {
                            tasks.add(
                                Task(
                                    title = input.title,
                                    body = input.body,
                                    isDone = false,
                                    creationDate = LocalDateTime.now(),
                                    dueDate = input.dueDate
                                )
                            )
                        } else {
                            editedTask.title = input.title
                            editedTask.body = input.body
                            editedTask.dueDate = input.dueDate
                        }
                        scope.launch {
                            bottomSheetState.hide()
                            onDismiss()
                        }
                    },
                    onCancel = {
                        scope.launch {
                            bottomSheetState.hide()
                            onDismiss()
                        }
                    }
                )
            }
        }
    }
}
