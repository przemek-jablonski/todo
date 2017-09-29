package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.utils.computation
import com.android.szparag.todoist.utils.flatMap
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith

class TodoistDayPresenter(private val model: CalendarModel) : TodoistBasePresenter<DayView>(), DayPresenter {

  override fun onViewReady() {
    logger.debug("onViewReady")
  }

  override fun onAttached() {
    super.onAttached()
    logger.debug("onAttached, model: $model (${model.hashCode()})")
    model.getSelectedDay()
        .computation()
        .filter { view != null }
        .flatMap { renderDay -> view!!.setupCalendarCheckList(renderDay).map { renderDay } }
        .flatMap { renderDay -> view!!.setupCalendarExtras(renderDay).map { renderDay } }
        .subscribeBy(
            onComplete = {
              logger.debug("model.getSelectedDay/view.setupWeekList.onNext")
            },
            onError = { exc ->
              logger.error("model.getSelectedDay/view.setupWeekList.onError", exc)
            })
        .toModelDisposable()
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }


}