package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.FrontView
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

private const val FRONT_LIST_LOADING_THRESHOLD = 4

class TodoistFrontPresenter(private val calendarModel: CalendarModel) : TodoistBasePresenter<FrontView>(), FrontPresenter {

  override fun onAttached() {
    super.onAttached()
    calendarModel.attach()
    logger.debug("onAttached")
  }

  override fun onBeforeDetached() {
    super.onBeforeDetached()
    calendarModel.detach()
    logger.debug("onBeforeDetached")
  }

  override fun onViewReady() {
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
        ?.doOnEach { outOfRangeDirection -> logger.debug("view?.subscribeDayListScrolls.onEach (FILTERED), direction: $outOfRangeDirection") }
//        ?.flatMap { outOfRangeDirection -> calendarModel.getRelativeWeekAsDays(outOfRangeDirection) }
        ?.doOnSubscribe {
          logger.debug("view?.subscribeDayListScrolls.onSubscribe")
          view?.addToDayList(calendarModel.getRelativeWeekAsDays(0))
        }
        ?.subscribeBy(onNext = { renderDays ->
          logger.debug("view?.subscribeDayListScrolls.onNext, renderDays: $renderDays")
//          view?.addToDayList(renderDays)
        }, onError = { exc ->
          logger.error("view?.subscribeDayListScrolls.onError, exc: $exc")
          calendarModel.resetRelativeWeekAsDays()
        }, onComplete = {
          logger.debug("view?.subscribeDayListScrolls.onComplete")
          calendarModel.resetRelativeWeekAsDays()
        })
  }

  private fun checkIfListOutOfRange(firstVisibleItemPos: Int, lastVisibleItemPos: Int, lastItemOnListPos: Int) = when {
    FRONT_LIST_LOADING_THRESHOLD >= firstVisibleItemPos -> -1
    lastVisibleItemPos >= lastItemOnListPos - FRONT_LIST_LOADING_THRESHOLD -> 1
    else -> 0
  }



  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")

  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}