package com.yuval.remepy_test.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yuval.remepy_test.R

@Composable
fun ActionBar(
    onAddTaskClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
            ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Add Task Button
            IconButton(
                modifier = Modifier
                    .width(120.dp)
                ,
                onClick = onAddTaskClick,
                shape = RoundedCornerShape(25),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.add_circle_24px),
                        contentDescription = "Add task",
                        tint = Color.Black
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Add Task",
                        color = Color.Black,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

        }
    }
}
