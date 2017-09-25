package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.views.contracts.DayView

class TodoistDayPresenter : TodoistBasePresenter<DayView>(), DayPresenter {
  override fun onViewReady() {
    logger.debug("onViewReady")
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}