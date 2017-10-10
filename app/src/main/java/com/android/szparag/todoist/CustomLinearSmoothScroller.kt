package com.android.szparag.todoist

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearSmoothScroller
import android.util.DisplayMetrics
import android.util.Log

class CustomLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

  val SCROLLING_TIME_CEIL_MILLIS = 150

  override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
    val result = super.computeScrollVectorForPosition(targetPosition)
    Log.d("RV", "computeScrollVectorForPosition, scrollVector: $result")
    return result
  }

  override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
    val MILLIS_PER_INCH = 100f //this val has to be assigned here every time, because android is fucking sloppy dumb bitch
    val densityDpi = displayMetrics.densityDpi
    val result = MILLIS_PER_INCH / densityDpi
    Log.d("RV", "calculateSpeedPerPixel, result: $result (millisPerInch: $MILLIS_PER_INCH, densityDpi: $densityDpi)")
    return result
  }


  override fun calculateTimeForDeceleration(dx: Int): Int {
    val result = super.calculateTimeForDeceleration(dx)
    Log.d("RV", "calculateTimeForDeceleration, dx: $dx, time: $result (millis)")
    return result
  }

  override fun calculateTimeForScrolling(dx: Int): Int {
    val resultSuper = super.calculateTimeForScrolling(dx)
    val resultAdjusted = if (resultSuper <= 10) resultSuper else minOf(resultSuper, SCROLLING_TIME_CEIL_MILLIS)
    Log.d("RV",
        "${hashCode()} calculateTimeForScrolling, dx: $dx, resultSuper: $resultSuper, resultAdjusted: $resultAdjusted, scrollingCeil: " +
            "$SCROLLING_TIME_CEIL_MILLIS")
    return resultAdjusted
  }
}