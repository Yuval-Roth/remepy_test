package com.yuval.remepy_test.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuval.remepy_test.R
import com.yuval.remepy_test.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

private val DueDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy, HH:mm")

@Composable
fun TaskCard(
    task: Task,
    onEdit: (Task) -> Unit,
    onMarkNotDone: (Task) -> Unit,
    onMarkDone: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val onMarkDone = { task: Task ->
        onMarkDone(task)
        scope.launch {
            delay(300)
            expanded = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RectangleShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 12.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp)
                    .clickable { expanded = !expanded }
                    .padding(start = 18.dp, top = 12.dp, bottom = 12.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                )

                Box {
                    ThreeDotButton(
                        onClick = { menuExpanded = true },
                    )
                    TaskActionsMenu(
                        expanded = menuExpanded,
                        isDone = task.isDone,
                        onDismiss = { menuExpanded = false },
                        onToggleDone = {
                            menuExpanded = false
                            if(task.isDone) {
                                onMarkNotDone(task)
                            } else {
                                onMarkDone(task)
                            }
                        },
                        onEdit = {
                            menuExpanded = false
                            onEdit(task)
                        },
                        onDelete = {
                            menuExpanded = false
                            onDelete(task)
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = EaseInOutQuad
                    ),
                    expandFrom = Alignment.Top
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = EaseInOutQuad
                    ),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, top = 5.dp, end = 15.dp)
                    ) {
                        Text(
                            text = task.body,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                        Text(
                            text = "Due date",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = task.dueDate.format(DueDateFormatter),
                            modifier = Modifier.padding(top = 4.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(
                        onClick = {
                            onMarkDone(task)
                            menuExpanded = false
                        },
                        enabled = !task.isDone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1FA463),
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(text = if (task.isDone) "Completed" else "Mark as done")
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskActionsMenu(
    expanded: Boolean,
    isDone: Boolean,
    onDismiss: () -> Unit,
    onToggleDone: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ) {
        DropdownMenuItem(
            text = { Text(if (isDone) "Mark as not done" else "Mark as done") },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                        id = if (isDone) R.drawable.undo_24px else R.drawable.check_24px
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            onClick = onToggleDone
        )
        if (!isDone) {
            DropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                onClick = onEdit
            )
        }
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.delete_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = onDelete
        )
    }
}

@Composable
private fun ThreeDotButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.more_vert_24px),
            contentDescription = "Task actions",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
