package me.rerere.rikkahub.ui.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import me.rerere.rikkahub.data.datastore.AnimationSetting
import me.rerere.ui.animation.AnimationCategory
import me.rerere.ui.animation.AnimationPreference

/** 把持久化的 [AnimationSetting] 转成运行时的 [AnimationPreference]。 */
@Composable
fun rememberAnimationPreference(setting: AnimationSetting): AnimationPreference =
    remember(setting) {
        val allowed = buildSet {
            if (setting.pageTransition) add(AnimationCategory.PageTransition)
            if (setting.contentTransition) add(AnimationCategory.ContentTransition)
            if (setting.loadingIndicator) add(AnimationCategory.LoadingIndicator)
            if (setting.expandCollapse) add(AnimationCategory.ExpandCollapse)
            if (setting.decorativeEffect) add(AnimationCategory.DecorativeEffect)
            if (setting.ripple) add(AnimationCategory.Ripple)
            if (setting.imeAnimation) add(AnimationCategory.ImeAnimation)
        }
        AnimationPreference(disableAll = setting.disableAll, allowed = allowed)
    }
