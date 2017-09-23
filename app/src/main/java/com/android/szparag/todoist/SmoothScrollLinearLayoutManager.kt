package com.android.szparag.todoist

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.graphics.PointF
import android.support.v7.widget.LinearSmoothScroller
import android.util.Log


class SmoothScrollLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

  private val MILLISECONDS_PER_INCH = 100f

  override fun smoothScrollToPosition(recyclerView: RecyclerView, state: State, position: Int) {
    val linearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {

      override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        val scrollVector = this@SmoothScrollLinearLayoutManager.computeScrollVectorForPosition(targetPosition)
        Log.d("RV", "scrollVector: $scrollVector")
        return scrollVector
      }

      override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics)
          = MILLISECONDS_PER_INCH / displayMetrics.densityDpi


    }

    linearSmoothScroller.targetPosition = position
    startSmoothScroll(linearSmoothScroller)
  }


}