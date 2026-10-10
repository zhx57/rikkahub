package me.rerere.ui.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

/**
 * 动画分类。设置页里的每个开关对应其中一个分类。
 */
enum class AnimationCategory {
    /** 页面转场：导航进出场、共享元素（hero）动画 */
    PageTransition,

    /** 内容切换：列表项出现/消失、交叉淡入淡出、模式切换 */
    ContentTransition,

    /** 加载指示：转圈、跳动圆点、微光骨架屏、Loading 图标 */
    LoadingIndicator,

    /** 展开折叠：思考过程展开、选择栏弹出等 */
    ExpandCollapse,

    /** 装饰性动效：网格渐变背景、emoji 彩带等纯装饰动画 */
    DecorativeEffect,

    /** 涟漪与按压反馈 */
    Ripple,
}

/**
 * 一次运行时的动画偏好快照，存放在 [LocalAnimationPreference] 里。
 */
@Immutable
class AnimationPreference(
    /** 总开关，打开后所有分类都不播动画 */
    val disableAll: Boolean,
    private val allowed: Set<AnimationCategory>,
) {
    fun enabled(category: AnimationCategory): Boolean = !disableAll && category in allowed

    companion object {
        /** 官方默认：全部动画正常播放 */
        val ALL: AnimationPreference =
            AnimationPreference(disableAll = false, allowed = AnimationCategory.entries.toSet())
    }
}

val LocalAnimationPreference: ProvidableCompositionLocal<AnimationPreference> =
    compositionLocalOf { AnimationPreference.ALL }

/** 某一类动画当前是否允许播放，已考虑总开关。 */
@Composable
@ReadOnlyComposable
fun animationEnabled(category: AnimationCategory): Boolean =
    LocalAnimationPreference.current.enabled(category)
