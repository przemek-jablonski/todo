package com.android.szparag.todoist

import android.view.View
import android.view.animation.Animation

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/17/2017.
 */

open class BaseAnimation: Animation()

inline fun View.animate() : BaseAnimation = BaseAnimation()

inline fun BaseAnimation.resize(): ResizeAnimation {
  return ResizeAnimation()
}