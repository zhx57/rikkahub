package me.rerere.rikkahub.ui.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 关闭软键盘弹出/收起的位移动画。
 *
 * 键盘动画和 Compose 里那些动画不一样：它由系统的 InsetsPolicy 驱动，
 * `Modifier.imePadding()` 只是在跟着系统的 inset 动画走，Compose 侧关不掉。
 * 应用层能做的正路只有一条——在系统的 `WindowInsetsAnimation` 派发时
 * 通知"动画已经结束"，让系统立刻把 inset 跳到终值，输入框直接就位。
 *
 * 提前结束动画需要 API 34（Android 14）的
 * `View.dispatchWindowInsetsAnimationFinished`。AndroidX 的
 * `dispatchAnimationFinished` 在低版本上会直接返回 false 不做处理，
 * 所以这个开关在 Android 14 以下不会生效，低版本仍会播放键盘动画。
 */
@Composable
fun DisableImeAnimation(enabled: Boolean) {
    if (!enabled) return
    val view = LocalView.current

    DisposableEffect(view, enabled) {
        val callback = object : WindowInsetsAnimationCompat.CallbackCompat() {
            override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                super.onPrepare(animation)
                // 只拦 IME 这类动画，系统栏动画照常播放
                if (animation.typeMask and WindowInsetsCompat.Type.ime() != 0) {
                    dispatchAnimationFinished(view, animation)
                }
            }
        }
        ViewCompat.setWindowInsetsAnimationCallback(view, callback)
        onDispose { ViewCompat.setWindowInsetsAnimationCallback(view, null) }
    }
}
