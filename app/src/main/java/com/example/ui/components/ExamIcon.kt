package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark

@Composable
fun ExamIcon(
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    iconSize: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation = 4.dp, shape = CircleShape, ambientColor = PrimaryBlue.copy(alpha = 0.2f), spotColor = PrimaryBlue.copy(alpha = 0.25f))
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PrimaryBlue,
                        PrimaryBlueDark
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = stringResource(R.string.exam_icon_description),
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

