package com.android.szparag.todoist.presenters

import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.WeekView
import io.reactivex.rxkotlin.subscribeBy

class TodoistWeekPresenter : TodoistBasePresenter<WeekView>(), WeekPresenter {

  private var dayOfTheWeekSelected = -1

  override fun onAttached() {
    super.onAttached()
    view?.setupWeekList()?.ui()?.subscribe().toViewDisposable()
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

  override fun subscribeViewUserEvents() { //todo: presenter has knowledge about View (it.first), refactor
    logger.debug("subscribeViewUserEvents")
    onUserDayPicked()
  }

  private fun onUserBackButtonPressed() {
//    view
//        ?.subscribeUserBackButtonPressed()
//        ?.ui()
//        ?.subscribeBy(
//            onNext = {
//              logger.debug()
//            },
//            onError = {
//              logger.error("onUserBackButtonPressed.subscribeUserBackButtonPressed")
//            },
//            onComplete = {
//              logger.debug()
//            }
//        )
  }

  private fun onUserDayPicked() {
    view?.run {
      this.subscribeUserDayPicked()
          .ui()
          .doOnNext { dayOfTheWeekSelected = it.second }
          .flatMap { this.animateWeekdayToFullscreen(it.first, it.second).ui() }
          .flatMapCompletable { this.animateShiftItemOnScreenPosition(dayOfTheWeekSelected).ui() }
          .doFinally { logger.debug("ItemClickSupport.subscribeItemClick.doFinally") }
          .subscribeBy(
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
