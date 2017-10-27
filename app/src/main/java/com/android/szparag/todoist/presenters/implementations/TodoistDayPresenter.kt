package com.android.szparag.todoist.presenters.implementations

import com.android.szparag.todoist.models.contracts.DayModel
import com.android.szparag.todoist.presenters.contracts.DayPresenter
import com.android.szparag.todoist.presenters.contracts.UnixTimestamp
import com.android.szparag.todoist.utils.invalidLongValue
import com.android.szparag.todoist.views.contracts.DayView
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class TodoistDayPresenter @Inject constructor(dayModel: DayModel) : TodoistBasePresenter<DayView, DayModel>(dayModel), DayPresenter {

  private var unixTimestamp = 1L

  override fun attach(view: DayView, dayUnixTimestamp: UnixTimestamp) {
    logger.debug("attach")
    check(dayUnixTimestamp >= 0)
    unixTimestamp = dayUnixTimestamp
    super.attach(view)
  }

  override fun attach(view: DayView) {
    super.attach(view)
    logger.debug("attach")
    attach(view, invalidLongValue())
  }

  override fun subscribeModelEvents() {
    logger.debug("subscribeModelEvents")
    model.getDayData(unixTimestamp)
        .subscribeBy(
            onSuccess = { renderDay ->
              view?.renderDay(renderDay.dayName, "${renderDay.dayNumber} ${renderDay.monthName} ${renderDay.yearNumber}", -1, -100)
            })


//    model.subscribeForDayData(unixTimestamp)
//        .subscribeBy(
//            onNext = { renderDay ->
//              logger.debug("model.subscribeForDayData.onNext: renderDay: $renderDay")
//              view?.renderDay(
//                  dayNameHeader = renderDay.dayName,
//                  dayCalendarSubtitle = "${renderDay.dayNumber} ${renderDay.monthName} ${renderDay.yearNumber}", //todo view should
//                  // accept this data and format it itself
//                  tasksCompletedCount = renderDay.tasksCompletedCount,
//                  tasksRemainingCount = renderDay.tasksRemainingCount
//              )
//            }
//        )
  }

  override fun subscribeViewUserEvents() {
    logger.debug("subscribeViewUserEvents")
  }

}