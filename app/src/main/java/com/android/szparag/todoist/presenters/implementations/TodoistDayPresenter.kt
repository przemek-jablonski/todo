package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.utils.computation
import com.android.szparag.todoist.views.contracts.DayView

class TodoistDayPresenter(private val model: CalendarModel) : TodoistBasePresenter<DayView>(), DayPresenter {

  override fun onViewReady() {
    logger.debug("onViewReady")
  }

  override fun onAttached() {
    super.onAttached()
    logger.debug("onAttached")
//    model.getCurrentWeek()
//        .computation()
//        .filter { view != null }
//        .flatMapCompletable }
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}