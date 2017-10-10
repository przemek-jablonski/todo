package com.android.szparag.todoist.views.implementations

import android.os.Bundle
import android.util.DisplayMetrics
import com.android.szparag.todoist.R
import com.android.szparag.todoist.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.utils.getDisplayMetrics
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class TodoistDayActivity : TodoistBaseActivity<DayPresenter>(), DayView {

  @Inject override lateinit var presenter: DayPresenter
  lateinit var displayMetrics: DisplayMetrics

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_day)
  }

  override fun onStart() {
    super.onStart()
    logger.debug("onStart")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this) //todo: find a way to generify them in Kotlin
    presenter.attach(this) //todo: find a way to generify them in Kotlin
    displayMetrics = getDisplayMetrics()
  }

  override fun onStop() {
    super.onStop()
    logger.debug("onStop")
    presenter.detach()
  }

  override fun setupCalendarHeader(headerImageUrl: String): Observable<Boolean> {
    logger.debug("setupCalendarHeader")
    return Observable.create {
      it.onNext(true)
    }
  }

  override fun setupCalendarQuote(quoteText: String): Observable<Boolean> {
    logger.debug("setupCalendarQuote")
    return Observable.create {
      it.onNext(true)
    }
  }

  override fun setupCalendarExtras(renderDay: RenderDay): Observable<Boolean> {
    logger.debug("setupCalendarExtras")
    return Observable.create {
      it.onNext(true)
    }
  }

  override fun setupCalendarCheckList(renderDay: RenderDay): Observable<Boolean> {
    logger.debug("setupCalendarCheckList")
    return Observable.create {
      it.onNext(true)
    }
  }

  override fun setupCalendarBackingView(renderDay: RenderDay): Observable<Boolean> {
    logger.debug("setupCalendarBackingView")
    return Observable.create {
      it.onNext(true)
    }
  }

  override fun subscribeUserAddButtonClicked(): Observable<Any> {
    logger.debug("subscribeUserAddButtonClicked")
    return Observable.create { }
  }

  override fun subscribeUserChecklistItemCheck(): Observable<Any> {
    logger.debug("subscribeUserChecklistItemCheck")
    return Observable.create { }
  }

  override fun subscribeUserChecklistItemLongPressed(): Observable<Any> {
    logger.debug("subscribeUserChecklistItemLongPressed")
    return Observable.create { }
  }

  override fun animateCalendarHeader(): Completable {
    logger.debug("animateCalendarHeader")
    return Completable.create { }
  }

  override fun animateCalendarQuote(): Completable {
    logger.debug("animateCalendarQuote")
    return Completable.create { }
  }

  override fun animateCalendarChecklist(): Completable {
    logger.debug("animateCalendarChecklist")
    return Completable.create { }
  }

  override fun animateCalendarAddButton(): Completable {
    logger.debug("animateCalendarAddButton")
    return Completable.create { }
  }

  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create { }
  }

}
