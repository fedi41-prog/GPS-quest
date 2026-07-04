package org.fedi4.gpsquest.core.ui.components

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// https://medium.com/@eozsahin1993/custom-progress-bars-in-jetpack-compose-723afb60c81c
@Composable
fun MultipleLinearProgressIndicator(
    modifier: Modifier = Modifier,
    primaryProgress: () -> Float,
    secondaryProgress: () -> Float,
    primaryColor: Color = MaterialTheme.colorScheme.tertiary.copy(),
    secondaryColor: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    clipShape: CornerBasedShape = RoundedCornerShape(3000.dp)
) {
    Box(
        modifier = modifier
            .clip(clipShape)
            .background(backgroundColor)

            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(secondaryColor, clipShape)
                .fillMaxHeight()
                .fillMaxWidth(secondaryProgress())
                .clip(clipShape)
        )
        Box(
            modifier = Modifier
                .background(primaryColor, clipShape)
                .fillMaxHeight()
                .fillMaxWidth(primaryProgress())
                .clip(clipShape)
        )
    }
}