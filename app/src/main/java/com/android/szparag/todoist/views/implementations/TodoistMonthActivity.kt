package com.android.szparag.todoist.views.implementations

import android.os.Bundle
import com.android.szparag.todoist.R
import com.android.szparag.todoist.presenters.contracts.MonthPresenter
import com.android.szparag.todoist.views.contracts.MonthView
import io.reactivex.Completable
import io.reactivex.Observable

class TodoistMonthActivity : TodoistBaseActivity<MonthPresenter>(), MonthView {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_todoist_month)
  }

  override fun setupMonthList(): Completable {
    logger.debug("setupMonthList")
    return Completable.create { }
  }

  override fun subscribeUserDayClicked(): Observable<Int> {
    logger.debug("subscribeUserDayClicked")
    return Observable.create { }
  }

  override fun subscribeUserDayLongClicked(): Observable<Int> {
    logger.debug("subscribeUserDayLongClicked")
    return Observable.create { }
  }

  override fun subscribeUserWeekClicked(): Observable<Int> {
    logger.debug("subscribeUserWeekClicked")
    return Observable.create { }
  }

  override fun subscribeUserWeekLongClicked(): Observable<Int> {
    logger.debug("subscribeUserWeekLongClicked")
    return Observable.create { }
  }

  override fun animateMonthListFadeIn(): Completable {
    logger.debug("animateMonthListFadeIn")
    return Completable.create { }
  }

  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    logger.debug("subscribeUserBackButtonPressed")
    return Observable.create { }
  }
}
