package com.android.szparag.todoist.widgets

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.android.szparag.todoist.utils.abs
import com.android.szparag.todoist.utils.max

class CustomizableRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

  //todo: snapping to element
  //todo: attrs from xml
  var flingVelocityXMultiplier = 1f
  var flingVelocityYMultiplier = 1f
  var flingVelocityXLimit = 25000
  var flingVelocityYLimit = 25000

  override fun fling(velocityX: Int, velocityY: Int) = super.fling(
      if (velocityX >= 0) processFlingX(velocityX) else -processFlingX(velocityX),
      if (velocityY >= 0) processFlingY(velocityY) else -processFlingY(velocityY)
  )

  fun processFlingX(velocityX: Int) = Math.min((velocityX.abs() * flingVelocityXMultiplier).toInt(), flingVelocityXLimit)

  fun processFlingY(velocityY: Int) = Math.min((velocityY.abs() * flingVelocityYMultiplier).toInt(), flingVelocityYLimit)
}