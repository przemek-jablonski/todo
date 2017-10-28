@file:Suppress("NOTHING_TO_INLINE")

package com.android.szparag.todoist.utils

import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SmoothScroller
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.Window
import com.android.szparag.todoist.ItemClickSupport
import com.android.szparag.todoist.R
import com.android.szparag.todoist.ResizeAnimation
import com.android.szparag.todoist.models.entities.TodoistTask
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.Random

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 9/17/2017.
 */

//fun Float.lerp(other: Float, amount: Float): Float = this + amount * (other - this)
//fun Int.lerp(other: Float, amount: Float): Float = this + amount * (other - this)

//fun lerp(a: Float, b: Float, amount: Float): Float {
//
//}

inline fun checkNotNull(vararg args: Any?): Boolean {
  args.forEach { arg ->
    if (arg == null) return false
  }
  return true
}

inline fun <A : Any, B : Any, C : Any> ifNotNull(arg1: A?, arg2: B?, arg3: C?, succeededBlock: (A, B, C) -> (Unit?)) =
    if (arg1 != null && arg2 != null && arg3 != null) {
      succeededBlock(arg1, arg2, arg3)
    } else {
      null
    }

inline fun LinearLayoutManager.getVisibleItemsPositions() = Pair(
    this.findFirstVisibleItemPosition(),
    this.findLastVisibleItemPosition()
)


inline fun View.setViewDimensions(itemWidth: Int?, itemHeight: Int?): View {
  if (itemWidth == null && itemHeight == null) return this
  this.layoutParams = this.layoutParams.apply {
    itemWidth?.let { this.width = it }
    itemHeight?.let { this.height = it }
  }
  return this
}

inline fun Activity.getStatusbarHeight(): Int {
  val visibleDisplayFrame = window.getVisibleDisplayFrame()
  val statusBarHeight = visibleDisplayFrame.top
  val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
  val titleBarHeight = contentViewTop - statusBarHeight
  return Math.abs(titleBarHeight)
}

inline fun Window.getVisibleDisplayFrame(): Rect {
  val displayFrame = Rect()
  this.decorView.getWindowVisibleDisplayFrame(displayFrame)
  return displayFrame
}

inline fun ViewPropertyAnimator.duration(durationMillis: Long) = this.apply { duration = durationMillis }
inline fun ViewPropertyAnimator.interpolator(
    timeInterpolator: TimeInterpolator) = this.apply { interpolator = timeInterpolator }

inline fun DateTime.unixTime(): Long = (this.millis / 1000F).toLong()


inline fun RecyclerView.setupGranularClickListener()
    = this.getTag(
    R.id.item_click_support) as ItemClickSupport? ?: ItemClickSupport().apply {
  this.attach(this@setupGranularClickListener)
}

inline fun RecyclerView.clearGranularClickListener() = (this.getTag(
    R.id.item_click_support) as ItemClickSupport?)?.apply { this.detach() }

inline fun Activity.getDisplayMetrics(): DisplayMetrics {
  val displayMetrics = DisplayMetrics()
  windowManager.defaultDisplay.getMetrics(displayMetrics)
  return displayMetrics
}

inline fun DisplayMetrics.getDisplayDimensions() = Pair(this.widthPixels, this.heightPixels)

inline fun Activity.getDisplayDimensions() = getDisplayMetrics().getDisplayDimensions()

inline fun RecyclerView.getSmoothScrollTime() {
  this.layoutManager
}

fun <T : Comparable<T>> rangeListOf(vararg ranges: Iterable<T>) = ranges.flatMap { it }

inline fun LinearLayoutManager.setupSmoothScrolling(context: Context, durationFactor: Float = 2F): LinearLayoutManager {
  val linearSmoothScroller = LinearSmoothScroller(context).configureDurationFactor(context, durationFactor)
  return object : LinearLayoutManager(context) {
    override fun startSmoothScroll(smoothScroller: SmoothScroller?) = super.startSmoothScroll(linearSmoothScroller)
  }
}

inline fun LinearSmoothScroller.configureDurationFactor(context: Context, durationFactor: Float): LinearSmoothScroller {
  return object : LinearSmoothScroller(context) {
    override fun calculateSpeedPerPixel(
        displayMetrics: DisplayMetrics) = 25f * durationFactor / displayMetrics.densityDpi
  }
}

inline fun View.resize(targetWidth: Int = this.width, targetHeight: Int = this.height)
    = ResizeAnimation(this, targetWidth, targetHeight)


inline fun View.scale(targetWidth: Int = this.width, targetHeight: Int = this.height)
    = this.resize(targetWidth, targetHeight)

inline fun emptyString() = ""
inline fun <E> emptyMutableList() = mutableListOf<E>()

inline fun LocalDate.weekAsDays() = (0..6).map { this.plusDays(it) }

inline fun <E> MutableList<E>.add(elements: Collection<E>) = this.addAll(elements)

inline fun <E> Collection<E>.boundary(forward: Boolean): E = if (forward) last() else first()

inline fun range(from: Int, to: Int) = if (from < to) (from..to) else (to..from)

inline fun Random.nextIntPositive(bound: Int = 64) = this.nextInt(bound - 1) + 1

inline fun <E> Collection<E>.asString(): String {
  val it = iterator()
  if (!it.hasNext())
    return "[]"

  val sb = StringBuilder()
  sb.append("\n[")
  while (true) {
    val e = it.next()
    sb.append(if (e === this) "(this Collection)" else e)
    if (!it.hasNext())
      return sb.append(']').toString()
    sb.append(',').append("\n")
  }
}

inline fun LocalDate.dayUnixTimestamp() = this.toDateTimeAtStartOfDay().millis

inline fun RealmList<TodoistTask>.asString() =
    StringBuilder("[").apply { this@asString.forEach { this.append("{${it.unixCreationTimestamp}, ${it.name}},") } }.toString()

inline fun <T : Comparable<T>> T.max(other: T) = if (this >= other) this else other
inline fun <T : Comparable<T>> T.min(other: T) = if (this <= other) this else other

inline fun Int.abs() = Math.abs(this)
inline fun Long.abs() = Math.abs(this)
inline fun Float.abs() = Math.abs(this)
inline fun Double.abs() = Math.abs(this)

inline fun invalidIntValue(): Int = -1
inline fun invalidLongValue(): Long = -1L
inline fun invalidFloatValue(): Float = -1.0f
inline fun invalidDoubleValue(): Double = -1.0

inline fun <E : RealmModel> Realm.debugAllObjects(objectClass: Class<E>, logger: Logger) {
  where(objectClass).findAll().forEachIndexed { item, index ->
    logger.debug("Realm.debugAllObjects, [$index]: $item")
  }
}