package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.FrontView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS

private const val FRONT_LIST_LOADING_THRESHOLD = 4

//todo: change to constructor injection
//todo: model should be FrontModel (presenter's own Model), not CalendarModel (Model of given feature)
//todo: refactor to interactor, or some fancy naming shit like that
class TodoistFrontPresenter(calendarModel: CalendarModel) : TodoistBasePresenter<FrontView, CalendarModel>(calendarModel), FrontPresenter {

  override fun attach(view: FrontView) {
    logger.debug("attach, view: $view")
    super.attach(view)
  }

  override fun onAttached() {
    logger.debug("onAttached")
    super.onAttached()
  }

  override fun onBeforeDetached() {
    logger.debug("onBeforeDetached")
    super.onBeforeDetached()
    model.detach()
  }

  override fun onViewReady() {
    logger.debug("onViewReady")
    super.onViewReady()

    view?.animateShowBackgroundImage()
        ?.ui()
        ?.filter { (eventType) -> eventType == END }
        ?.flatMap { view?.animateShowQuote()?.ui() }
        ?.filter { (eventType) -> eventType == END }
        ?.flatMap { view?.animatePeekCalendar()?.ui() } //todo: in submodelevents
        ?.filter { (eventType) -> eventType == END }
        ?.subscribe()
        ?.toViewDisposable()

    view?.subscribeDayListScrolls()
        ?.ui()
        ?.doOnEach { scrollEvent -> logger.debug("view?.subscribeDayListScrolls.onEach, event: $scrollEvent") }
        ?.map { checkIfListOutOfRange(it.firstVisibleItemPos, it.lastVisibleItemPos, it.lastItemOnListPos) }
        ?.filter { direction -> direction != 0 }
        ?.doOnEach { outOfRangeDirection ->
          logger.debug("view?.subscribeDayListScrolls.onEach (FILTERED), direction: $outOfRangeDirection")
        }
        ?.doOnSubscribe {
          logger.debug("view?.subscribeDayListScrolls.onSubscribe")
          model.fillDaysListInitial()
        }
        ?.subscribeBy(onNext = { direction ->
          logger.debug("view?.subscribeDayListScrolls.onNext, direction: $direction")
          model.requestRelativeWeekAsDays(direction > 0, 2)
        }, onError = { exc ->
          logger.error("view?.subscribeDayListScrolls.onError, exc: $exc")
        }, onComplete = {
          logger.debug("view?.subscribeDayListScrolls.onComplete")
        })
  }

  //todo: why this shit is here
  private fun checkIfListOutOfRange(firstVisibleItemPos: Int, lastVisibleItemPos: Int, lastItemOnListPos: Int) = when {
    FRONT_LIST_LOADING_THRESHOLD >= firstVisibleItemPos -> -1
    lastVisibleItemPos >= lastItemOnListPos - FRONT_LIST_LOADING_THRESHOLD -> 1
    else -> 0
  }


  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
    model.subscribeForDaysList()
        .ui()
        .doOnSubscribe { logger.debug("calendarModel.subscribeForDaysList.onSubscribe") }
        .subscribeBy(
            onNext = { event ->
              logger.debug("calendarModel.subscribeForDaysList.onNext, event: $event")
            },
            onError = { exc ->
              logger.error("calendarModel.subscribeForDaysList.onError, exc: $exc")
            },
            onComplete = {
              logger.debug("calendarModel.subscribeForDaysList.onComplete")
            }
        )
        .toModelDisposable()
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}