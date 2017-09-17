@file:Suppress("NOTHING_TO_INLINE")

package com.android.szparag.todoist

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SmoothScroller
import android.support.v7.widget.RecyclerView.State
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/17/2017.
 */

inline fun LinearLayoutManager.setupSmoothScrolling(context: Context, durationFactor: Float = 2F): LinearLayoutManager {
  val linearSmoothScroller = LinearSmoothScroller(context).configureDurationFactor(context, durationFactor)
  return object: LinearLayoutManager(context) {
    override fun startSmoothScroll(smoothScroller: SmoothScroller?) {
      super.startSmoothScroll(linearSmoothScroller)
    }
  }
}

inline fun LinearSmoothScroller.configureDurationFactor(context: Context, durationFactor: Float): LinearSmoothScroller {
  return object: LinearSmoothScroller(context) {
    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
      return 25f * durationFactor / displayMetrics.densityDpi
    }
  }
}

inline fun View.resize(targetWidth: Int = this.width, targetHeight: Int = this.height)
    = ResizeAnimation(this, targetWidth, targetHeight)


inline fun View.scale(targetWidth: Int = this.width, targetHeight: Int = this.height)
    = this.resize(targetWidth, targetHeight)