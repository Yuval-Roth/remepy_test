package com.yuval.remepy_test.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuval.remepy_test.model.Task
import java.time.format.DateTimeFormatter

private val DueDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy, HH:mm")

@Composable
fun TaskCard(task: Task) {
    var expanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 6.dp
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
                    color = if (task.isDone) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                )

                Box {
                    ThreeDotButton(
                        onClick = { menuExpanded = true },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TaskActionsMenu(
                        expanded = menuExpanded,
                        isDone = task.isDone,
                        onDismiss = { menuExpanded = false },
                        onMarkDone = {
                            task.isDone = true
                            menuExpanded = false
                        },
                        onEdit = { menuExpanded = false },
                        onDelete = { menuExpanded = false }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f))
                        .padding(start = 18.dp, top = 14.dp, end = 18.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = task.body,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                    Button(
                        onClick = { task.isDone = true },
                        enabled = !task.isDone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(6.dp),
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
    onMarkDone: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ) {
        DropdownMenuItem(
            text = { Text(if (isDone) "Done" else "Mark as done") },
            leadingIcon = { CheckIcon(enabled = !isDone) },
            enabled = !isDone,
            onClick = onMarkDone
        )
        DropdownMenuItem(
            text = { Text("Edit") },
            leadingIcon = { EditIcon() },
            onClick = onEdit
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { DeleteIcon() },
            onClick = onDelete
        )
    }
}

@Composable
private fun ThreeDotButton(
    onClick: () -> Unit,
    color: Color
) {
    IconButton(onClick = onClick) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val radius = 2.4.dp.toPx()
            val centerX = size.width / 2f
            val spacing = 6.5.dp.toPx()
            drawCircle(color = color, radius = radius, center = Offset(centerX, size.height / 2f - spacing))
            drawCircle(color = color, radius = radius, center = Offset(centerX, size.height / 2f))
            drawCircle(color = color, radius = radius, center = Offset(centerX, size.height / 2f + spacing))
        }
    }
}

@Composable
private fun CheckIcon(enabled: Boolean) {
    val color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    Canvas(modifier = Modifier.size(20.dp)) {
        drawLine(
            color = color,
            start = Offset(size.width * 0.18f, size.height * 0.52f),
            end = Offset(size.width * 0.42f, size.height * 0.74f),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.42f, size.height * 0.74f),
            end = Offset(size.width * 0.82f, size.height * 0.28f),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun EditIcon() {
    val color = MaterialTheme.colorScheme.onSurfaceVariant
    Canvas(modifier = Modifier.size(20.dp)) {
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.75f),
            end = Offset(size.width * 0.74f, size.height * 0.26f),
            strokeWidth = 2.2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.66f, size.height * 0.18f),
            end = Offset(size.width * 0.82f, size.height * 0.34f),
            strokeWidth = 2.2.dp.toPx(),
            cap = StrokeCap.Round
        )
        val tip = Path().apply {
            moveTo(size.width * 0.20f, size.height * 0.80f)
            lineTo(size.width * 0.34f, size.height * 0.76f)
            lineTo(size.width * 0.24f, size.height * 0.66f)
            close()
        }
        drawPath(path = tip, color = color)
    }
}

@Composable
private fun DeleteIcon() {
    val color = MaterialTheme.colorScheme.error
    Canvas(modifier = Modifier.size(20.dp)) {
        drawLine(
            color = color,
            start = Offset(size.width * 0.25f, size.height * 0.34f),
            end = Offset(size.width * 0.75f, size.height * 0.34f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.36f, size.height * 0.44f),
            end = Offset(size.width * 0.40f, size.height * 0.80f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.64f, size.height * 0.44f),
            end = Offset(size.width * 0.60f, size.height * 0.80f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.30f, size.height * 0.38f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.40f, size.height * 0.48f),
            style = Stroke(width = 1.8.dp.toPx())
        )
    }
}
