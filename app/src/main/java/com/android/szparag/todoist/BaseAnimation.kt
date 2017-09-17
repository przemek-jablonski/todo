package com.android.szparag.todoist

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.REPEAT
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.START
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 17/09/2017.
 */
abstract class BaseAnimation(internal val targetView: View) : Animation() {

  private val listenerSubject : Subject<AnimationEvent> = PublishSubject.create()

  init {
    setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationRepeat(animation: Animation?)
          = listenerSubject.onNext(AnimationEvent(REPEAT))

      override fun onAnimationEnd(animation: Animation?)
          = listenerSubject.onNext(AnimationEvent(END))

      override fun onAnimationStart(animation: Animation?)
          = listenerSubject.onNext(AnimationEvent(START))
    })
  }

  fun play(): Observable<AnimationEvent> {
    targetView.startAnimation(this)
    return listenerSubject
  }

  fun pause(): BaseAnimation {
    targetView.clearAnimation()
    return this
  }

  fun duration(durationMillis: Int = 500, interpolator: AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()) : BaseAnimation{
    this.duration = durationMillis.toLong()
    this.interpolator = interpolator
    return this
  }

  override final fun setAnimationListener(listener: AnimationListener?) {
    super.setAnimationListener(listener)
  }

}