package com.android.szparag.todoist

import android.view.View
import android.view.animation.Transformation

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/16/2017.
 */
class ResizeAnimation(targetView: View, private val targetWidth: Int, private val targetHeight: Int)
  : BaseAnimation(targetView) {

  private val startWidth: Int = targetView.width
  private val startHeight: Int = targetView.height

  override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
    super.applyTransformation(interpolatedTime, t)
    val newHeight = startHeight + (targetHeight - startHeight) * interpolatedTime
    targetView.layoutParams.height = newHeight.toInt()
    targetView.requestLayout()
  }

  override fun willChangeBounds(): Boolean = true

}