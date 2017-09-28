package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.AnimationEvent
import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.presenters.contracts.WeekPresenter
import com.android.szparag.todoist.utils.computation
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.View.Screen.DAY_SCREEN
import com.android.szparag.todoist.views.contracts.View.Screen.MONTH_SCREEN
import com.android.szparag.todoist.views.contracts.WeekView
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

class TodoistWeekPresenter(private val model: CalendarModel) : TodoistBasePresenter<WeekView>(), WeekPresenter {

  override fun onAttached() {
    super.onAttached()
    logger.debug("onAttached, model: $model (${model.hashCode()})")
    model.getCurrentWeek()
        .computation()
        .filter { view != null }
        .flatMapCompletable { view!!.setupWeekList(it).ui() }
        .subscribeBy(
            onComplete = {
              logger.debug("model.getCurrentWeek/view.setupWeekList.onNext")
            },
            onError = { exc ->
              logger.error("model.getCurrentWeek/view.setupWeekList.onError", exc)
            })
        .toModelDisposable()
  }

  override fun onBeforeDetached() {
    super.onBeforeDetached()
    logger.debug("onBeforeDetached")
    view?.disposeWeekList()?.subscribe()
  }

  override fun onViewReady() {
    logger.debug("onViewReady")
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
    model
        .attach()
        .computation()
        .subscribeBy(
            onComplete = {
              logger.debug("subscribeModelEvents.model.attach.onComplete")
            },
            onError = {
              logger.error("subscribeModelEvents.model.attach.onError")
            }
        )
  }

  override fun subscribeViewUserEvents() { //todo: presenter has knowledge about View (it.first), refactor
    logger.debug("subscribeViewUserEvents")
    onUserBackButtonPressed()
    onUserDayPicked()
  }

  private fun onUserBackButtonPressed() {
    view
        ?.subscribeUserBackButtonPressed()
        ?.ui()
        ?.subscribeBy(
            onNext = {
              logger.debug("onUserBackButtonPressed.subscribeUserBackButtonPressed.onNext")
              view?.gotoScreen(MONTH_SCREEN)
            },
            onError = { exc -> logger.error("onUserBackButtonPressed.subscribeUserBackButtonPressed.onError", exc) },
            onComplete = { logger.debug("onUserBackButtonPressed.subscribeUserBackButtonPressed.onComplete") }
        )
  }

  private fun onUserDayPicked() {
    var dayNumberInTheWeekSelected: Int = -1
    view?.run {
      this.subscribeUserDayPicked()
          .ui()
          .doOnNext { dayNumberInTheWeekSelected = it.second }
          .doOnNext { "onUserDayPicked.subscribeUserDayPicked.doOnNext, dayNumber: ${it.second}" }
          .flatMap { this.animateWeekdayToFullscreen(it.first, it.second).ui() }
          .doOnNext { this.animateShiftItemOnScreenPosition(dayNumberInTheWeekSelected).ui().subscribe() }
//          .mergeWith{ model.setSelectedDay(dayNumberInTheWeekSelected) }
          .doOnNext { model.setSelectedDaySync(dayNumberInTheWeekSelected) }
          .delay(500, TimeUnit.MILLISECONDS)
          .subscribeBy(
              onNext = { animationEvent ->
                logger.debug("onUserDayPicked.subscribeUserDayPicked.onNext, animEvent: $animationEvent")
                when (animationEvent.eventType) {
                  AnimationEvent.AnimationEventType.START -> {
                  }
                  AnimationEvent.AnimationEventType.END -> view?.gotoScreen(DAY_SCREEN)
                  AnimationEvent.AnimationEventType.REPEAT -> {
                  }
                }
              },
              onError = { exc -> logger.error("onUserDayPicked.subscribeUserDayPicked.onError", exc) },
              onComplete = { logger.debug("onUserDayPicked.subscribeUserDayPicked.onComplete") }
          )
          .toViewDisposable()
    }
  }


}
