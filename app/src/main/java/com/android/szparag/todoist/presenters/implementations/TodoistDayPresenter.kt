package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.utils.computation
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.rxkotlin.subscribeBy

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
        .flatMapCompletable { view!!.setupCalendarBackingView(it).ui() }
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