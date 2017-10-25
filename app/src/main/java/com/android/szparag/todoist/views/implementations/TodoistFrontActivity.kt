package com.android.szparag.todoist.views.implementations

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.REPEAT
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.START
import com.android.szparag.todoist.FrontTestAdapter
import com.android.szparag.todoist.ItemClickSupport
import com.android.szparag.todoist.R
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.events.ListScrollEvent
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.asString
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.utils.duration
import com.android.szparag.todoist.utils.getDisplayDimensions
import com.android.szparag.todoist.utils.getStatusbarHeight
import com.android.szparag.todoist.utils.interpolator
import com.android.szparag.todoist.views.contracts.FrontView
import com.android.szparag.todoist.views.contracts.UnixTimestamp
import com.android.szparag.todoist.widgets.TodoistRecyclerView
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.nvanbenschoten.motion.ParallaxImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Observable

private const val DAYS_RECYCLER_VELOCITY_MULTIPLIER = 0.45f
private const val DAYS_RECYCLER_VELOCITY_LIMIT = 7000

class TodoistFrontActivity : TodoistBaseActivity<FrontPresenter>(), FrontView {

  private val backgroundImage: ParallaxImageView by bindView(R.id.imageViewFrontBackground)
  private val quoteText: TextView by bindView(R.id.textViewFrontQuote)
  private val quoteTextBackground: View by bindView(R.id.gradientTopText)
  private val daysRecycler: TodoistRecyclerView by bindView(R.id.recyclerViewFront)
  private val daysRecyclerBackground: View by bindView(R.id.gradientBottomRecycler)
  private lateinit var daysRecyclerAdapter: FrontTestAdapter
  private val daysLayoutManager: LinearLayoutManager by lazy {
    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    logger.debug("onCreate, bundle: $savedInstanceState")
    setContentView(R.layout.activity_todoist_front)
    val displayDimensions = getDisplayDimensions()
    daysRecyclerAdapter = FrontTestAdapter(
        (displayDimensions.first * 0.66f).toInt())
    daysRecyclerAdapter.setHasStableIds(true)
    daysRecycler.flingVelocityXMultiplier = DAYS_RECYCLER_VELOCITY_MULTIPLIER
    daysRecycler.flingVelocityXLimit = DAYS_RECYCLER_VELOCITY_LIMIT
    daysRecycler.adapter = daysRecyclerAdapter
    /*,(displayDimensions.second * 0.50f).toInt()*/
    daysRecycler.layoutManager = daysLayoutManager
    LinearSnapHelper().attachToRecyclerView(daysRecycler)
    daysRecycler.setHasFixedSize(true)
    daysRecycler.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING)
  }

  override fun onStart() {
    super.onStart()
    logger.debug("onStart")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
    presenter.attach(this)
  }

  override fun setupViews() {
    super.setupViews()
    logger.debug("setupViews")
    quoteText.visibility = View.VISIBLE
    quoteText.y -= quoteText.height + getStatusbarHeight()
    daysRecycler.scrollToPosition(6) //todo this should be in presenter AND it sucks
  }

  override fun onResume() {
    super.onResume()
    backgroundImage.setTiltSensitivity(0.60f)
    backgroundImage.setParallaxIntensity(1.35f)
  }

  override fun onPause() {
    super.onPause()
    backgroundImage.unregisterSensorManager()
  }

  override fun animateShowBackgroundImage(): Observable<AnimationEvent> {
    logger.debug("animateShowBackgroundImage")
    return Observable.create { emitter ->
      //todo here check if internet is in place or whatever
      randomizeContents()
      backgroundImage.animate()
          .alpha(1F)
          .duration(1750)
          .interpolator(DecelerateInterpolator())
          .setListener(object : AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
              logger.debug("animateShowBackgroundImage.onAnimationRepeat")
              emitter.onNext(AnimationEvent(REPEAT))
            }

            override fun onAnimationEnd(animation: Animator?) {
              logger.debug("animateShowBackgroundImage.onAnimationEnd")
              emitter.onNext(AnimationEvent(END))
              backgroundImage.registerSensorManager(16600)
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

  override fun subscribeBackgroundClicked() = RxView.clicks(backgroundImage)

  override fun randomizeContents() {
    randomizePhoto()
    randomizeQuote()
  }

  private fun randomizePhoto() {
    val backgroundPlaceholdersArray = resources.obtainTypedArray(R.array.background_placeholders)
    val randomResourceId = backgroundPlaceholdersArray.getResourceId(
        (Math.random() * backgroundPlaceholdersArray.length()).toInt(), R.drawable.background_placeholder_5)
    Picasso.with(this)
        .load(randomResourceId)
        .priority(Picasso.Priority.HIGH)
        .fetch(object : Callback {
          override fun onSuccess() {
            logger.debug("onSuccess")
            Picasso.with(this@TodoistFrontActivity).load(randomResourceId).into(backgroundImage)
          }

          override fun onError() {
            logger.debug("onError")
          }
        })
    backgroundPlaceholdersArray.recycle()
  }

  //todo feature: in settings - change background effect (PicassoTransformations)
  private fun randomizeQuote() {
    val quotePlaceholdersContentsArray = resources.getStringArray(R.array.quote_placeholders_content)
    val quotePlaceholdersAuthorsArray = resources.getStringArray(R.array.quote_placeholders_author)
    val randomResourceIndex = (Math.random() * quotePlaceholdersContentsArray.size).toInt()
    val randomContent = quotePlaceholdersContentsArray[randomResourceIndex]
    val randomAuthor = quotePlaceholdersAuthorsArray[randomResourceIndex]
    quoteText.text = "\" $randomContent \" - $randomAuthor"
    quoteTextBackground.layoutParams = quoteTextBackground.layoutParams.apply { this.height = (quoteText.height * 1.50F).toInt() }
    quoteTextBackground.animate()
        .alpha(1F)
        .setStartDelay(250)
        .duration(1000)
        .interpolator(AccelerateInterpolator())
        .start()
  }

  override fun animateShowQuote(): Observable<AnimationEvent> {
    logger.debug("animateShowQuote")
    return Observable.create { emitter ->
      logger.debug("animateShowQuote.run")
      randomizeQuote()
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
      daysRecyclerBackground.layoutParams = daysRecyclerBackground.layoutParams.apply { this.height = (daysRecycler.height * 2F).toInt() }
      daysRecyclerBackground.animate()
          .alpha(1F)
          .duration(750)
          .interpolator(AccelerateInterpolator())
          .start()
    }
  }


  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create { }
  }

  override fun subscribeDayListScrolls(): Observable<ListScrollEvent> {
    logger.debug("subscribeDayListScrolls")
    return RxRecyclerView.scrollEvents(daysRecycler)
        .concatMap { rvScrollEvent ->
          RxRecyclerView.scrollStateChanges(daysRecycler)
              .map { scrollStateInt ->
                ListScrollEvent(
                    rvScrollEvent.dx(),
                    rvScrollEvent.dy(),
                    scrollStateInt,
                    daysLayoutManager.findFirstVisibleItemPosition(),
                    daysLayoutManager.findLastVisibleItemPosition(),
                    daysLayoutManager.itemCount
                )
              }
        }
  }

  override fun appendRenderDays(appendingDays: Collection<RenderDay>, fromIndex: Int, changedElementsCount: Int) {
    logger.debug("appendRenderDays, appendingDays: ${appendingDays.asString()}, fromIndex: $fromIndex, changedElementsCount: $changedElementsCount")
    daysRecyclerAdapter.updateData(appendingDays, fromIndex, changedElementsCount)
  }

  override fun subscribeDayClicked(): Observable<UnixTimestamp> {
    logger.debug("subscribeDayClicked")
    return daysRecycler.subscribeItemClicks()
  }


}
