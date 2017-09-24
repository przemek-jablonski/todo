package com.android.szparag.todoist.presenters

import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import com.android.szparag.todoist.ui
import com.android.szparag.todoist.views.contracts.WeekView
import io.reactivex.rxkotlin.subscribeBy

class TodoistWeekPresenter : TodoistBasePresenter<WeekView>(), WeekPresenter {

  private var dayOfTheWeekSelected = -1

  override fun onAttached() {
    super.onAttached()
    view?.setupWeekdaysList()
  }

  override fun onBeforeDetached() {
    super.onBeforeDetached()
  }

  override fun onViewReady() {
    logger.debug("onViewReady")
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")

    view?.run {
      this.onUserDayPicked()
          .ui()
          .doOnNext { dayOfTheWeekSelected = it.second }
          .flatMap { this.resizeDayToFullscreen(it.first, it.second).ui() }
          .subscribeBy(
              onNext = {
                logger.debug("ItemClickSupport.subscribeItemClick.onNext: ")
                this.fixPositionByScrolling(dayOfTheWeekSelected)
//                handleWeekItemClicked(calendarWeekRecyclerView, it.second, it.first)
              },
              onError = {
                logger.error("ItemClickSupport.subscribeItemClick.onError: exc: $it")
              },
              onComplete = {
                logger.debug("ItemClickSupport.subscribeItemClick.onComplete")
              }
          )
          .toViewDisposable()
    }

  }

}
