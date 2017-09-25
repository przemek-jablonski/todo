package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.presenters.contracts.MonthPresenter
import com.android.szparag.todoist.views.contracts.MonthView

class TodoistMonthPresenter: TodoistBasePresenter<MonthView>(), MonthPresenter {
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