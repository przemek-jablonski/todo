package com.android.szparag.todoist

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearSmoothScroller
import android.util.DisplayMetrics
import android.util.Log
import com.android.szparag.todoist.utils.Logger
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class CustomLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

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
    val result = super.calculateTimeForScrolling(dx)
    Log.d("RV", "${hashCode()} calculateTimeForScrolling, dx: $dx, time(total,ms): $result")
    return result
  }
}