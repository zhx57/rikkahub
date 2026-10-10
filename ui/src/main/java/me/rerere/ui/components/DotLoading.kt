package me.rerere.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.rerere.ui.animation.AnimationCategory
import me.rerere.ui.animation.animationEnabled

@Preview
@Composable
fun DotLoading(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = 600,
    size: Dp = 16.dp,
) {
    // 关掉加载动画后保持一个固定的中间透明度，不再无限循环
    val animated = animationEnabled(AnimationCategory.LoadingIndicator)
    val alpha = if (animated) {
        val infiniteTransition = rememberInfiniteTransition(label = "dot_loading")
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animationDuration),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dot_alpha"
        ).value
    } else {
        0.65f
    }

    Box(
        modifier = Modifier
            .size(size)
            .then(modifier)
            .alpha(alpha)
            .background(color = color, shape = CircleShape)
    )
}
