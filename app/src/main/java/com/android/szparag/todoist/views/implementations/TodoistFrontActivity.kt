package com.android.szparag.todoist.views.implementations

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.REPEAT
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.START
import com.android.szparag.todoist.FrontTestAdapter
import com.android.szparag.todoist.R
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ListScrollEvent
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.duration
import com.android.szparag.todoist.utils.flatMap
import com.android.szparag.todoist.utils.getDisplayDimensions
import com.android.szparag.todoist.utils.getStatusbarHeight
import com.android.szparag.todoist.utils.interpolator
import com.android.szparag.todoist.views.contracts.FrontView
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import javax.inject.Inject

class TodoistFrontActivity : TodoistBaseActivity<FrontPresenter>(), FrontView {

  private val backgroundImage: ImageView by bindView(R.id.imageViewFrontBackground)
  private val quoteText: TextView by bindView(R.id.textViewFrontQuote)
  private val daysRecycler: RecyclerView by bindView(R.id.recyclerViewFront)
  private val daysLayoutManager: LinearLayoutManager by lazy {
    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
  }

  @Inject override lateinit var presenter: FrontPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    logger.debug("onCreate, bundle: $savedInstanceState")
    setContentView(R.layout.activity_todoist_front)
  }

  override fun onStart() {
    super.onStart()
    logger.debug("onStart")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this) //todo: find a way to generify them in Kotlin
    presenter.attach(this) //todo: find a way to generify them in Kotlin
  }

  override fun setupViews() {
    super.setupViews()
    logger.debug("setupViews")
    quoteText.visibility = View.VISIBLE
    quoteText.y -= quoteText.height + getStatusbarHeight()

    val displayDimensions = getDisplayDimensions()
    daysRecycler.adapter = FrontTestAdapter(
        (displayDimensions.first * 0.66f).toInt()
        /*,(displayDimensions.second * 0.50f).toInt()*/)
    daysRecycler.layoutManager = daysLayoutManager
    LinearSnapHelper().attachToRecyclerView(daysRecycler)
  }

  override fun animateShowBackgroundImage(): Observable<AnimationEvent> {
    logger.debug("animateShowBackgroundImage")
    return Observable.create { emitter ->
      backgroundImage.animate()
          .alpha(1F)
          .duration(1750)
          .interpolator(AccelerateDecelerateInterpolator())
          .setListener(object : AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
              logger.debug("animateShowBackgroundImage.onAnimationRepeat")
              emitter.onNext(AnimationEvent(REPEAT))
            }

            override fun onAnimationEnd(animation: Animator?) {
              logger.debug("animateShowBackgroundImage.onAnimationEnd")
              emitter.onNext(AnimationEvent(END))
            }

            override fun onAnimationCancel(animation: Animator?) {
              logger.debug("animateShowBackgroundImage.onAnimationCancel")
              emitter.onNext(AnimationEvent(END))
            }

            override fun onAnimationStart(animation: Animator?) {
              logger.debug("animateShowBackgroundImage.onAnimationStart")
              emitter.onNext(AnimationEvent(START))
              backgroundImage.visibility = View.VISIBLE
            }
          })
          .start()
    }
  }

  override fun animateShowQuote(): Observable<AnimationEvent> {
    logger.debug("animateShowQuote")
    return Observable.create { emitter ->
      logger.debug("animateShowQuote.run")
      quoteText.animate()
          .translationY(0F)
          .setDuration(1000)
          .setInterpolator(OvershootInterpolator(0.75f))
          .setListener(object : AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
              logger.debug("animateShowQuote.onAnimationRepeat")
              emitter.onNext(AnimationEvent(REPEAT))
            }

            override fun onAnimationEnd(animation: Animator?) {
              logger.debug("animateShowQuote.onAnimationEnd")
              emitter.onNext(AnimationEvent(END))
            }

            override fun onAnimationCancel(animation: Animator?) {
              logger.debug("animateShowQuote.onAnimationCancel")
              emitter.onNext(AnimationEvent(END))
            }

            override fun onAnimationStart(animation: Animator?) {
              logger.debug("animateShowQuote.onAnimationStart")
              emitter.onNext(AnimationEvent(START))
            }

          })
          .start()
    }
  }

  override fun animatePeekCalendar(): Observable<AnimationEvent> {
    logger.debug("animatePeekCalendar")
    return Observable.create { emitter ->
      emitter.onNext(AnimationEvent(END))
    }
  }


  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create { }
  }

  //todo: this return type is leaking separation concern (you wouldn't have RecyclerViewScrollEvent in iOS)
  override fun subscribeDayListScrolls(): Observable<ListScrollEvent> {
    logger.debug("subscribeDayListScrolls")
    //todo: every rxbinding call has a strong reference to given view, it should be disposed (or is it done
    //todo: automagically?)
    return RxRecyclerView.scrollEvents(daysRecycler)
        .map { recyclerViewScrollEvent -> ListScrollEvent(recyclerViewScrollEvent.dx(), recyclerViewScrollEvent.dy()) }
        .flatMap { RxRecyclerView.scrollStateChanges(daysRecycler).map{ stateInt ->
          it.apply { this.setState(stateInt)} }}
//        .flatMap { RxRecyclerView.scrollStateChanges(daysRecycler) }
//        .flatMap(RxRecyclerView.scrollStateChanges(daysRecycler), BiFunction { t1, t2 -> ListScrollEvent() })
//    return RxRecyclerView.scrollEvents(daysRecycler).mergeWith { RxRecyclerView.scrollStateChanges(daysRecycler) }
  }

}
