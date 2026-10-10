package me.rerere.rikkahub.ui.animation

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView

/**
 * 关闭软键盘弹出/收起的位移动画。
 *
 * 键盘动画和 Compose 里的那些不是一回事：它由系统的 InsetsPolicy 驱动，
 * `Modifier.imePadding()` 只是跟着系统的 inset 动画逐帧走，在 Compose 侧关不掉。
 *
 * 应用层能做的正路是在系统的 [WindowInsetsAnimation.Callback] 里，
 * 动画刚开始时就通知系统"这段动画已经结束"，系统会立刻把 inset 跳到终值，
 * 输入框直接就位，不再有上滑的过程。
 *
 * 用到的 [View.dispatchWindowInsetsAnimationEnd] 从 API 30（Android 11）就有了，
 * 更低的版本系统没有这套动画回调，这个开关不会有任何效果。
 */
@Composable
fun DisableImeAnimation(enabled: Boolean) {
    if (!enabled) return
    val view = LocalView.current

    DisposableEffect(view, enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            installImeAnimationSkipper(view)
        }
        onDispose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                view.setWindowInsetsAnimationCallback(null)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun installImeAnimationSkipper(view: View) {
    view.setWindowInsetsAnimationCallback(
        object : WindowInsetsAnimation.Callback(
            WindowInsetsAnimation.Callback.DISPATCH_MODE_CONTINUE_ON_SUBTREE
        ) {
            override fun onPrepare(animation: WindowInsetsAnimation) {
                super.onPrepare(animation)
                // 只拦 IME 这类动画，系统栏动画照常播放
                if (animation.typeMask and WindowInsets.Type.ime() != 0) {
                    view.dispatchWindowInsetsAnimationEnd(animation)
                }
            }

            // 抽象方法，必须实现：原样返回，不额外消费 inset
            override fun onProgress(
                insets: WindowInsets,
                animations: MutableList<WindowInsetsAnimation>,
            ): WindowInsets = insets
        }
    )
}
