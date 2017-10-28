package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.AnimationEvent.AnimationEventType.END
import com.android.szparag.todoist.models.contracts.FrontModel
import com.android.szparag.todoist.models.entities.RenderDay
import com.android.szparag.todoist.presenters.contracts.FrontPresenter
import com.android.szparag.todoist.utils.ReactiveList.ReactiveChangeType.INSERTED
import com.android.szparag.todoist.utils.ReactiveListEvent
import com.android.szparag.todoist.utils.ui
import com.android.szparag.todoist.views.contracts.FrontView
import com.android.szparag.todoist.views.contracts.View.Screen.DAY_SCREEN
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

private const val FRONT_LIST_LOADING_THRESHOLD = 4

//todo: change to constructor injection
//todo: model should be FrontModel (presenter's own Model), not CalendarModel (Model of given feature)
//todo: refactor to interactor, or some fancy naming shit like that
class TodoistFrontPresenter @Inject constructor(frontModel: FrontModel) : TodoistBasePresenter<FrontView, FrontModel>(frontModel),
    FrontPresenter {

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
        ?.doOnEach { scrollEvent -> logger.debug("view?.subscribeDayListScrolls.onNext, scrollEvent: $scrollEvent") }
        ?.map { checkIfListOutOfRange(it.firstVisibleItemPos, it.lastVisibleItemPos, it.lastItemOnListPos) }
        ?.doOnEach { logger.debug("after filtering, directionInt: $it") }
        ?.filter { direction -> direction != 0 }
        ?.doOnSubscribe {
          model.loadDaysFromCalendar(true, 2) //todo this should be in subscribeModelsEvents or something
          model.loadDaysFromCalendar(false, 2) //todo or on attaching model to presenter
        }
        ?.subscribeBy(onNext = { direction ->
          logger.debug("view?.subscribeDayListScrolls.onNext, direction: $direction")
          onUserReachedListLoadThreshold(direction)
        }, onComplete = {
          logger.debug("view?.subscribeDayListScrolls.onComplete")
        })
        .toViewDisposable()
  }

  override fun onUserReachedListLoadThreshold(direction: Int) {
    model.loadDaysFromCalendar(weekForward = direction > 0, weeksCount = 2)
  }

  //todo: why this shit is here
  private fun checkIfListOutOfRange(firstVisibleItemPos: Int, lastVisibleItemPos: Int, lastItemOnListPos: Int): Int {
    return when {
      FRONT_LIST_LOADING_THRESHOLD >= firstVisibleItemPos -> -1
      lastVisibleItemPos >= lastItemOnListPos - FRONT_LIST_LOADING_THRESHOLD -> 1
      else -> 0
    }.also {
      logger.info("checkIfListOutOfRange, RESULT: $it (start: 0, firstVisible: $firstVisibleItemPos, lastVisible: " +
          "$lastVisibleItemPos, last: $lastItemOnListPos)")
    }
  }

  private fun onNewItemsToCalendarLoaded(event: ReactiveListEvent<RenderDay>) {
    if (event.eventType == INSERTED) { view?.appendRenderDays(event.affectedItems, event.fromIndexInclusive, event.affectedItems.size) }
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")

    model.subscribeForDaysList()
        .ui()
        .doOnSubscribe { logger.debug("calendarModel.subscribeForDaysListData.onSubscribe") }
        .subscribeBy(
            onNext = { event ->
              logger.debug("calendarModel.subscribeForDaysListData.onNext, list: $event")
              onNewItemsToCalendarLoaded(event)
            },
            onComplete = {
              logger.debug("calendarModel.subscribeForDaysListData.onComplete")
            }
        )
        .toModelDisposable()
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")

    //todo debug only, remove
    view?.subscribeBackgroundClicked()
        ?.ui()
        ?.subscribe { view?.randomizeContents() }
        .toViewDisposable()

    view?.subscribeDayClicked()
        ?.ui()
        ?.doOnDispose { logger.warn("view?.subscribeDayClicked(), DISPOSED")}
        ?.subscribe {
          logger.debug("view?.subscribeDayClicked(), ondayClicked")
          dayClicked(it)
        }
        .toViewDisposable()
  }

  private fun dayClicked(dayUnixTimestamp: Long) {
    logger.debug("dayClicked: $dayUnixTimestamp")
    view?.goToDayScreen(dayUnixTimestamp)
  }


}