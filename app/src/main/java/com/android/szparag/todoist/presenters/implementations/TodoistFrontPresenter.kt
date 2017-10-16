package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.models.contracts.CalendarModel
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.FrontView
import io.reactivex.rxkotlin.subscribeBy

private const val FRONT_LIST_LOADING_THRESHOLD = 4

class TodoistFrontPresenter(private val calendarModel: CalendarModel) : TodoistBasePresenter<FrontView>(), FrontPresenter {

//  //todo: this fun should be not accessible for children
//  //todo: figure out what to do with passed models, they have to be attached or initialized here, not in onAttached()
//  override fun attach(view: FrontView) {
//    super.attach(view)
//  }

  override fun onAttached() {
    calendarModel.attach()
    super.onAttached()
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
        ?.filter { direction -> direction != 0 }
        ?.doOnEach { outOfRangeDirection ->
          logger.debug("view?.subscribeDayListScrolls.onEach (FILTERED), direction: $outOfRangeDirection")
        }
        ?.doOnSubscribe {
          logger.debug("view?.subscribeDayListScrolls.onSubscribe")
        }
        ?.subscribeBy(onNext = { direction ->
          logger.debug("view?.subscribeDayListScrolls.onNext, direction: $direction")
          calendarModel.requestRelativeWeekAsDays(direction > 0, 2)
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

    calendarModel.fetchRelativeWeekAsDays()
        .ui()
        .subscribeBy(
            onNext = { renderDay ->
              logger.debug("calendarModel.fetchRelativeWeekAsDays().onNext, event: $renderDay")
              view?.addToDayList(renderDay)
            },
            onError = { exc ->
              logger.debug("calendarModel.fetchRelativeWeekAsDays().onError, exc: $exc")
            },
            onComplete = {
              logger.debug("calendarModel.fetchRelativeWeekAsDays().onComplete")
            }
        )


  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}