package com.android.szparag.todoist.views.implementations

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.R
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.events.PermissionEvent
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import com.android.szparag.todoist.utils.bindView
import com.android.szparag.todoist.views.contracts.FrontView
import com.android.szparag.todoist.views.contracts.View.Screen
import io.reactivex.Observable
import javax.inject.Inject

class TodoistFrontActivity : TodoistBaseActivity<FrontPresenter>(), FrontView {

  private val backgroundImage: ImageView by bindView(R.id.image_front_background)
  private val quoteText: TextView by bindView(R.id.textview_front_quote)


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

  override fun animateShowBackgroundImage(): Observable<AnimationEvent> {
    logger.debug("animateShowBackgroundImage")
    return Observable.create { emitter ->
      backgroundImage.visibility = View.VISIBLE
      emitter.onNext(AnimationEvent(END))
    }
  }

  override fun animateShowQuote(): Observable<AnimationEvent> {
    logger.debug("animateShowQuote")
    return Observable.create { emitter ->
      quoteText.visibility = View.VISIBLE
      emitter.onNext(AnimationEvent(END))

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

}
